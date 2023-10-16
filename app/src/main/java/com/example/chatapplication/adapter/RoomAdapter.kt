package com.example.chatapplication.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.activity.ChatActivity
import com.example.chatapplication.databinding.ItemRoomBinding
import com.example.chatapplication.dto.RoomInfo

class RoomAdapter:RecyclerView.Adapter<RoomAdapter.ViewHolder>() {
    val items = ArrayList<RoomInfo>()

    inner class ViewHolder(val binding:ItemRoomBinding):RecyclerView.ViewHolder(binding.root){
        init {
            binding.root.setOnClickListener {

                val intent = Intent(binding.root.context,ChatActivity::class.java)
                intent.putExtra("userId",items[adapterPosition].otherUserId)
                binding.root.context.startActivity(intent, null)

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomAdapter.ViewHolder {
        val view = ItemRoomBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: RoomAdapter.ViewHolder, position: Int) {
        holder.binding.apply {
            imageView.setImageURI(Uri.parse(items[position].otherUserInfo.imageUri))
            userNameTextView.text = items[position].otherUserInfo.userName
            contentTextView.text = items[position].lastContent
            writtenTimeTextView.text = items[position].lastWrittenTime
        }
    }


    override fun getItemCount() = items.size
}