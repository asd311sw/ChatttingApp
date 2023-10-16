package com.example.chatapplication.adapter

import android.content.Intent
import android.media.RouteListingPreference.Item
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.activity.ProfileActivity
import com.example.chatapplication.databinding.ItemFollowerBinding
import com.example.chatapplication.dto.FollowerInfo

class FollowerAdapter:RecyclerView.Adapter<FollowerAdapter.ViewHolder>() {
    val items = ArrayList<FollowerInfo>()

    inner class ViewHolder(val binding:ItemFollowerBinding):RecyclerView.ViewHolder(binding.root){
        init {

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ProfileActivity::class.java)
                intent.putExtra("userId",items[adapterPosition].userId)

                binding.root.context.startActivity(intent,null)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFollowerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            imageView.setImageURI(Uri.parse(items[position].imageUri))
            userNameTextView.text = items[position].userName
        }

    }

    override fun getItemCount() = items.size
}