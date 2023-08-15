package com.innoji.petabencana.ui.maps

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.innoji.petabencana.R
import com.innoji.petabencana.data.network.response.GeometriesItem
import com.innoji.petabencana.databinding.ActivityMapsBinding
import com.innoji.petabencana.helper.ProvinceHelper
import com.innoji.petabencana.helper.SettingPreferences
import com.innoji.petabencana.helper.ViewModelFactory
import com.innoji.petabencana.ui.maps.adapter.SearchReportAdapter
import com.innoji.petabencana.ui.setting.SettingActivity
import com.innoji.petabencana.ui.setting.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.apache.commons.text.WordUtils


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels()
    private var disaster: String? = null
    private lateinit var adapter: SearchReportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttonListener()
        bottomSheet()
        searchBar()
        darkMode()


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    private fun darkMode(){
        val pref = SettingPreferences.getInstance(dataStore)
        val themeViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]
        themeViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }


    private fun searchBar(){
        with(binding) {
            searchBar.setOnMenuItemClickListener{menuItem ->
                when(menuItem.itemId){
                    R.id.menu1 ->{
                        val intent = Intent(this@MapsActivity, SettingActivity::class.java)
                        startActivity(intent)
                    }
                }
                true
            }

            searchView.setupWithSearchBar(searchBar)
            searchView.editText.setOnEditorActionListener { _, _, _ ->
                searchBar.text = searchView.text
                searchView.hide()
                if(searchView.text != null) {
                    viewModelSetup()
                }
                false
            }

        }
    }

    private fun bottomSheet(){
        val sheet = findViewById<FrameLayout>(R.id.sheet)
        val bottomSheetBehavior = BottomSheetBehavior.from(sheet)
        bottomSheetBehavior.peekHeight = 650
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSheetBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when(newState){
                    BottomSheetBehavior.STATE_EXPANDED -> {

                    }
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> {

                    }
                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })
    }

    private fun viewModelSetup(){
        val dataAdmin: String?

        dataAdmin = binding.searchView.text.toString()
        val dataAdminCapital = WordUtils.capitalizeFully(dataAdmin)


        val provinceList = ProvinceHelper.provinceList
        var selectedValueKey: String? = null
        for(province in provinceList){
            if(dataAdminCapital == province.nama){
                selectedValueKey = province.valueKey
                break
            }else{
                selectedValueKey = dataAdminCapital
            }
        }

        if(selectedValueKey == ""){
            selectedValueKey = null
        }

        if(selectedValueKey != null && disaster != null){
            viewModel.searchReport(selectedValueKey, disaster)
        }else if(selectedValueKey != null){
            viewModel.searchReport(selectedValueKey, null)
        }else if(disaster != null){
            viewModel.searchReport(null, disaster)
        }else{
            viewModel.searchReport(null, null)
        }

        viewModel.reportData.observe(this){reportData ->
            if(reportData != null) {
                setDataSearchReport(reportData)
            }
        }

        viewModel.returnResponse.observe(this){
            if(it == 400){
                adapter.clear()
                val imgNoData = findViewById<ImageView>(R.id.ivNoData)
                imgNoData.visibility = View.VISIBLE
                Toast.makeText(this, "data tidak ditemukan", Toast.LENGTH_SHORT).show()
                mMap.clear()
            }else if (it == 200){
                val imgNoData = findViewById<ImageView>(R.id.ivNoData)
                imgNoData.visibility = View.GONE
            }
        }

        viewModel.isLoading.observe(this){
            showLoading(it)
        }
    }

    private fun setDataSearchReport(searchReport: List<GeometriesItem>){

        val listReport = ArrayList<GeometriesItem>()
        adapter = SearchReportAdapter(listReport)
        adapter.clear()
        mMap.clear()

        for(report in searchReport){
            val latLng = LatLng(report.coordinates[1], report.coordinates[0])
            mMap.addMarker(MarkerOptions().position(latLng).title(report.properties.disasterType))
            listReport.add(report)
        }

        val firstCoordinate = searchReport[0]
        val latLng = LatLng(firstCoordinate.coordinates[1], firstCoordinate.coordinates[0])
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

        val rvBottomSheet = findViewById<RecyclerView>(R.id.rvReports)
        rvBottomSheet.adapter = adapter
        showRecycleView(rvBottomSheet, LinearLayoutManager.VERTICAL)
    }

    private fun showRecycleView(rv: RecyclerView, orientation: Int) {
        rv.layoutManager = LinearLayoutManager(this, orientation,false)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    private fun buttonListener(){
        val btnFlood = findViewById<MaterialButton>(R.id.btnFlood)
        val btnEarthquake = findViewById<MaterialButton>(R.id.btnEartquake)
        val btnFire = findViewById<MaterialButton>(R.id.btnFire)
        val btnHaze = findViewById<MaterialButton>(R.id.btnHaze)
        val btnWind = findViewById<MaterialButton>(R.id.btnWind)
        val btnVolcano = findViewById<MaterialButton>(R.id.btnVulcano)
        val btnDefault = findViewById<MaterialButton>(R.id.btnDefault)

        btnDefault.setOnClickListener {
            btnFlood.isEnabled = true
            btnEarthquake.isEnabled = true
            btnFire.isEnabled = true
            btnHaze.isEnabled = true
            btnWind.isEnabled = true
            btnVolcano.isEnabled = true

            disaster = null

            btnFlood.setBackgroundColor(Color.WHITE)
            btnEarthquake.setBackgroundColor(Color.WHITE)
            btnFire.setBackgroundColor(Color.WHITE)
            btnHaze.setBackgroundColor(Color.WHITE)
            btnVolcano.setBackgroundColor(Color.WHITE)
            btnWind.setBackgroundColor(Color.WHITE)
        }
        btnFlood.setOnClickListener {
            toggleButtonBackground(btnFlood)
            btnEarthquake.isEnabled = false
            btnFire.isEnabled = false
            btnHaze.isEnabled = false
            btnWind.isEnabled = false
            btnVolcano.isEnabled = false
        }
        btnEarthquake.setOnClickListener {
            toggleButtonBackground(btnEarthquake)
            btnFlood.isEnabled = false
            btnFire.isEnabled = false
            btnHaze.isEnabled = false
            btnWind.isEnabled = false
            btnVolcano.isEnabled = false
        }
        btnFire.setOnClickListener {
            toggleButtonBackground(btnFire)
            btnFlood.isEnabled = false
            btnEarthquake.isEnabled = false
            btnHaze.isEnabled = false
            btnWind.isEnabled = false
            btnVolcano.isEnabled = false
        }
        btnHaze.setOnClickListener {
            toggleButtonBackground(btnHaze)
            btnFlood.isEnabled = false
            btnEarthquake.isEnabled = false
            btnFire.isEnabled = false
            btnWind.isEnabled = false
            btnVolcano.isEnabled = false
        }
        btnWind.setOnClickListener {
            toggleButtonBackground(btnWind)
            btnFlood.isEnabled = false
            btnEarthquake.isEnabled = false
            btnFire.isEnabled = false
            btnHaze.isEnabled = false
            btnVolcano.isEnabled = false
        }
        btnVolcano.setOnClickListener {
            toggleButtonBackground(btnVolcano)
            btnFlood.isEnabled = false
            btnEarthquake.isEnabled = false
            btnFire.isEnabled = false
            btnHaze.isEnabled = false
            btnWind.isEnabled = false
        }

    }

    private fun toggleButtonBackground(button: MaterialButton) {
        if(button.isEnabled){
            val tag = button.tag
            disaster = tag.toString()
            button.isEnabled = false
        }
        if (isButtonGreen(button)) {
            button.setBackgroundColor(Color.WHITE)
        } else{
            button.setBackgroundColor(Color.GREEN)
        }
    }

    private fun isButtonGreen(button: MaterialButton): Boolean {
        val currentColor = button.backgroundTintList?.defaultColor ?: Color.WHITE
        val greenColor = Color.parseColor("#4CAF50")
        return currentColor == greenColor
    }
}