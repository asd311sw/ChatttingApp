package com.example.chatapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.impl.model.WorkTag
import com.example.chatapplication.StartActivity
import com.example.chatapplication.UploadWorker
import com.example.chatapplication.activity.ChatActivity
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.databinding.ActivityChatBinding
import com.example.chatapplication.databinding.ItemChatBinding
import com.example.chatapplication.dto.ChatInfo
import java.util.concurrent.TimeUnit


class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ViewHolder>() {
    val items = ArrayList<ChatInfo>()
    private val users = LoginActivity.users
    private val otherUserId = ChatActivity.otherUserId



    inner class ViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {
        init {



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatInfo = items[position]
        setKakaoLoginUser(chatInfo,holder)
        setGoogleLoginUser(chatInfo,holder)

//        chatBinding.scrollView.post {
//            chatBinding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
//        }

    }

    override fun getItemCount() = items.size


    private fun setKakaoLoginUser(chatInfo: ChatInfo,holder: ViewHolder){
        val userId = chatInfo.userId

        LoginActivity.kakaoCurrentUser?.let {
            if (userId == it.id.toString()) {
                holder.binding.apply {
                    userChatTextView.visibility = View.INVISIBLE
                    userTimeTextView.visibility = View.INVISIBLE
                    myChatTextView.visibility = View.VISIBLE
                    myTimeTextView.visibility = View.VISIBLE
                    myChatTextView.text = chatInfo.message
                    myTimeTextView.text = chatInfo.currentTime
                }


            }
            else{
                holder.binding.apply {
                    myChatTextView.visibility = View.INVISIBLE
                    myTimeTextView.visibility = View.INVISIBLE
                    userChatTextView.visibility = View.VISIBLE
                    userTimeTextView.visibility = View.VISIBLE
                    userChatTextView.text = chatInfo.message
                    userTimeTextView.text = chatInfo.currentTime
                }


            }
        }
    }

    private fun setGoogleLoginUser(chatInfo: ChatInfo,holder: ViewHolder){
        val userId = chatInfo.userId

        LoginActivity.googleCurrentUser?.let {
            if (userId == it.id.toString()) {
                holder.binding.apply {
                    userChatTextView.visibility = View.INVISIBLE
                    userTimeTextView.visibility = View.INVISIBLE
                    myChatTextView.visibility = View.VISIBLE
                    myTimeTextView.visibility = View.VISIBLE
                    myChatTextView.text = chatInfo.message
                    myTimeTextView.text = chatInfo.currentTime
                }



            }
            else{
                holder.binding.apply {
                    myChatTextView.visibility = View.INVISIBLE
                    myTimeTextView.visibility = View.INVISIBLE
                    userChatTextView.visibility = View.VISIBLE
                    userTimeTextView.visibility = View.VISIBLE
                    userChatTextView.text = chatInfo.message
                    userTimeTextView.text = chatInfo.currentTime
                }


            }
        }
    }


}