package com.innoji.petabencana.ui.maps.adapter

import android.content.Context
import android.widget.ArrayAdapter
import com.innoji.petabencana.data.network.response.Province

class SuggestionSearchAdapter(context: Context, resource: Int, private val data: List<Province>):ArrayAdapter<String>(context, resource) {

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): String? {
        return data[position].nama
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun getProvinceValueKey(position: Int): String {
        return data[position].valueKey
    }
}