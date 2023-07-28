package com.innoji.petabencana.ui.maps.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.innoji.petabencana.data.network.response.GeometriesItem
import com.innoji.petabencana.data.network.response.Properties
import com.innoji.petabencana.databinding.ItemReportsBinding
import com.innoji.petabencana.helper.formatDate
import java.util.TimeZone

class SearchReportAdapter(private val searchReport: ArrayList<GeometriesItem>): RecyclerView.Adapter<SearchReportAdapter.ViewHolder>() {

    private var disasterList = listOf<GeometriesItem>()

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = ItemReportsBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            tvAuthor.text = searchReport[position].properties.text
            tvDate.text = formatDate(searchReport[position].properties.createdAt, TimeZone.getDefault().id)
            Glide.with(itemView.context).load(searchReport[position].properties.imageUrl).centerCrop().into(ivImage)
        }
    }

    override fun getItemCount(): Int = searchReport.size

    class ViewHolder(binding: ItemReportsBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivImage = binding.ivImage
        val tvAuthor = binding.tvAuthor
        val tvDate = binding.tvDate
    }

    fun clear(){
        searchReport.clear()
        notifyDataSetChanged()
    }


    interface OnItemClickCallback {
        fun onItemClicked(data: GeometriesItem)
    }
}