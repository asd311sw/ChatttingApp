package com.example.chatapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.databinding.ItemNotificationBinding
import com.example.chatapplication.dto.NotificationInfo
import java.sql.Array

class NotificationAdapter:RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    val items = ArrayList<NotificationInfo>()

    inner class ViewHolder(val binding:ItemNotificationBinding):RecyclerView.ViewHolder(binding.root){
        init {


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            imageView.setImageURI(Uri.parse(items[position].imageUri))
            contentTextView.text = items[position].content
            timeTextView.text = items[position].receivedTime
        }

    }


    override fun getItemCount() = items.size

}