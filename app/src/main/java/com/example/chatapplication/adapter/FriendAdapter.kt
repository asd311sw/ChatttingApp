package com.example.chatapplication.adapter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.NetworkReceiver
import com.example.chatapplication.R
import com.example.chatapplication.StartActivity
import com.example.chatapplication.activity.ChatActivity
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.activity.ProfileActivity
import com.example.chatapplication.databinding.ItemFriendBinding
import com.example.chatapplication.dto.FriendInfo
import com.example.chatapplication.dto.UserInfo
import com.example.chatapplication.fragment.FriendFragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FriendAdapter : RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    val items = ArrayList<FriendInfo>()

    private val users = LoginActivity.users
    private lateinit var userId: String
    private lateinit var myInfo: UserInfo

    init {

        LoginActivity.kakaoCurrentUser?.let {
            userId = it.id.toString()
        }


        LoginActivity.googleCurrentUser?.let {
            userId = it.id.toString()
        }

        getMyUserInfo()
    }

    inner class ViewHolder(val binding: ItemFriendBinding) : RecyclerView.ViewHolder(binding.root) {
        private var otherUserInfo = UserInfo("","","","","","","","")

        init {

            binding.root.setOnClickListener {

                val userId = items[adapterPosition].userId
                val intent = Intent(binding.root.context, ProfileActivity::class.java)
                intent.putExtra("userId", userId)

                binding.root.context.startActivity(intent, null)


            }


            binding.followButton.setOnClickListener {

                users.child(items[adapterPosition].userId).child("profile").get().addOnCompleteListener {
                    if(it.isComplete){
                        val result = it.result.value

                        val userInfo = (result as Map<*,*>).values.first() as Map<*,*>
                        val userId = userInfo.get("userId").toString()
                        val userName = userInfo.get("userName").toString()
                        val introduce = userInfo.get("introduce").toString()
                        val state = userInfo.get("state").toString()
                        val birthDate = userInfo.get("birthDate").toString()
                        val personality = userInfo.get("personality").toString()
                        val sex = userInfo.get("sex").toString()
                        val imageUri = userInfo.get("imageUri").toString()
                        val storyCount = userInfo.get("storyCount").toString().toInt()
                        val followingCount = userInfo.get("followingCount").toString().toInt()
                        val followerCount = userInfo.get("followerCount").toString().toInt()


                        otherUserInfo = UserInfo(
                            userId,
                            userName,
                            introduce,
                            state,
                            birthDate,
                            personality,
                            sex,
                            imageUri,
                            storyCount,
                            followingCount,
                            followerCount
                        )
                        otherUserInfo.followerCount += 1

                        users.child(items[adapterPosition].userId).child("profile").removeValue()
                        users.child(items[adapterPosition].userId).child("profile").push().setValue(otherUserInfo)

                    }
                }

                //otherUserInfo = getOtherUserInfo(items[adapterPosition].userId)

                LoginActivity.kakaoCurrentUser?.let {
                    val userId = it.id.toString()
                    myInfo.followingCount += 1
                    LoginActivity.myInfo.followingCount = myInfo.followingCount
                    users.child(userId).child("profile").removeValue()
                    users.child(userId).child("profile").push().setValue(myInfo)

                    users.child(userId).child("following").push().setValue(items[adapterPosition])
                    users.child(items[adapterPosition].userId).child("follower").push()
                        .setValue(myInfo)

                }

                //구글 로그인
                LoginActivity.googleCurrentUser?.let {
                    val userId = it.id.toString()
                    myInfo.followingCount += 1
                    LoginActivity.myInfo.followingCount = myInfo.followingCount
                    users.child(userId).child("profile").removeValue()
                    users.child(userId).child("profile").push().setValue(myInfo)

                    users.child(userId).child("following").push().setValue(items[adapterPosition])
                    users.child(items[adapterPosition].userId).child("follower").push()
                        .setValue(myInfo)
                }
            }

            binding.chatButton.setOnClickListener {

                val intent = Intent(binding.root.context, ChatActivity::class.java)
                intent.putExtra("userId", items[adapterPosition].userId)
                binding.root.context.startActivity(intent, null)
            }

            binding.callButton.setOnClickListener {

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            userNameTextView.text = items[position].userName
            imageView.setImageURI(Uri.parse(items[position].imageUri))

        }

    }

    override fun getItemCount() = items.size


    private fun getMyUserInfo() {
        users.child(userId).child("profile").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val userInfo = snapshot.value as Map<*, *>
                val userId = userInfo.get("userId").toString()
                val userName = userInfo.get("userName").toString()
                val introduce = userInfo.get("introduce").toString()
                val state = userInfo.get("state").toString()
                val birthDate = userInfo.get("birthDate").toString()
                val personality = userInfo.get("personality").toString()
                val sex = userInfo.get("sex").toString()
                val imageUri = userInfo.get("imageUri").toString()
                val storyCount = userInfo.get("storyCount").toString().toInt()
                val followingCount = userInfo.get("followingCount").toString().toInt()
                val followerCount = userInfo.get("followerCount").toString().toInt()


                myInfo = UserInfo(
                    userId,
                    userName,
                    introduce,
                    state,
                    birthDate,
                    personality,
                    sex,
                    imageUri,
                    storyCount,
                    followingCount,
                    followerCount
                )
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })
    }

//
//    private fun getOtherUserInfo(userId: String):UserInfo {
//
//        var otherUserInfo = UserInfo("","","","","","","","")
//
//
//        users.child(userId).child("profile")
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (data in snapshot.children) {
//                        var userInfo = data.value as Map<*, *>
//                        val otherUserId = userInfo.get("userId").toString()
//                        val userName = userInfo.get("userName").toString()
//                        val introduce = userInfo.get("introduce").toString()
//                        val state = userInfo.get("state").toString()
//                        val birthDate = userInfo.get("birthDate").toString()
//                        val personality = userInfo.get("personality").toString()
//                        val sex = userInfo.get("sex").toString()
//                        val imageUri = userInfo.get("imageUri").toString()
//                        val storyCount = userInfo.get("storyCount").toString().toInt()
//                        val followingCount = userInfo.get("followingCount").toString().toInt()
//                        val followerCount = userInfo.get("followerCount").toString().toInt()
//
//
//                        otherUserInfo = UserInfo(
//                            otherUserId,
//                            userName,
//                            introduce,
//                            state,
//                            birthDate,
//                            personality,
//                            sex,
//                            imageUri,
//                            storyCount,
//                            followingCount,
//                            followerCount
//                        )
//
//
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//
//            })
//
//
//        return otherUserInfo
//
//
//    }

}