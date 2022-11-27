package com.example.rssfeed.adapters

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.rssfeed.R
import com.example.rssfeed.database.RssFeedItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL


class MyCallback(var oldDataSet: List<RssFeedItem>, var newDataSet: List<RssFeedItem>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldDataSet.size
    }

    override fun getNewListSize(): Int {
        return newDataSet.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataSet.get(oldItemPosition) == newDataSet.get(newItemPosition)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataSet.get(oldItemPosition).equals(newDataSet.get(newItemPosition))
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}

class RssFeedRecyclerViewAdapter(private var dataSet: List<RssFeedItem>) : RecyclerView.Adapter<RssFeedRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val ivRssFeed: ImageView

        init {
            tvTitle = view.findViewById(R.id.tvTitle)
            ivRssFeed = view.findViewById(R.id.ivRssFeed)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rssfeed_rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = dataSet[position].title
        Glide.with(holder.ivRssFeed).load(dataSet[position].enclosure).placeholder(R.drawable.ic_banner_foreground).into(holder.ivRssFeed)
    }

    override fun getItemCount(): Int = dataSet.size

    fun update(newdataSet: List<RssFeedItem>){
        var diffResult = DiffUtil.calculateDiff(MyCallback(dataSet, newdataSet), true)
        dataSet = newdataSet
        diffResult.dispatchUpdatesTo(this)
    }

}