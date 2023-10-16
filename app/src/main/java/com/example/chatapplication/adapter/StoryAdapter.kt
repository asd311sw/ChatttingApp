package com.example.chatapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.ItemStoryBinding
import com.example.chatapplication.dto.StoryInfo
import java.lang.reflect.Array

class StoryAdapter:RecyclerView.Adapter<StoryAdapter.ViewHolder>() {
    val items = ArrayList<StoryInfo>()

    inner class ViewHolder(val binding: ItemStoryBinding):RecyclerView.ViewHolder(binding.root){
        init {

            binding.cardView.setOnClickListener {

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            imageView.setImageURI(Uri.parse(items[position].imageUri))
        }

    }

    override fun getItemCount() = items.size

}