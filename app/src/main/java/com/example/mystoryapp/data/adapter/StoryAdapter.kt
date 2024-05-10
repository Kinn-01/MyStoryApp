package com.example.mystoryapp.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.retrofit.response.ListStoryItem

class StoryAdapter (
    private val storiesList: MutableList<ListStoryItem>,
    private val listener: OnAdapterListener
) : RecyclerView.Adapter<StoryAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val photo: ImageView = itemView.findViewById(R.id.ivStory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_story, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = storiesList[position]
        holder.name.text = item.name
        Glide.with(holder.itemView.context)
            .load(item.photoUrl)
            .error(R.drawable.ic_launcher_background)
            .into(holder.photo)

        holder.itemView.setOnClickListener {
            listener.onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return storiesList.size
    }

    interface OnAdapterListener {
        fun onClick(story: ListStoryItem)
    }
}