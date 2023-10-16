package com.example.chatapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.StartActivity
import com.example.chatapplication.adapter.FollowerAdapter
import com.example.chatapplication.databinding.ActivityFollowerBinding
import com.example.chatapplication.dto.FollowerInfo
import com.example.chatapplication.dto.FollowingInfo
import com.google.api.Distribution.BucketOptions.Linear
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FollowerActivity : AppCompatActivity() {

    private val binding: ActivityFollowerBinding by lazy {
        ActivityFollowerBinding.inflate(layoutInflater)
    }

    private val followerAdapter by lazy {
        FollowerAdapter()
    }

    private val users = LoginActivity.users
    private val myInfo = MainActivity.myInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        bindView()

    }

    private fun initView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter = followerAdapter
        }

    }

    private fun bindView() {


        binding.closeButton.setOnClickListener {
            finish()
        }

        val userId = myInfo.userId
        val title = "follower"

        users.child(userId).child(title).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {

                    val userInfo = data.value as Map<*, *>

                    val otherUserId = userInfo.get("userId").toString()
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


                    followerAdapter.items.add(
                        FollowerInfo(
                            otherUserId,
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
                    )

                    followerAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

    }




}