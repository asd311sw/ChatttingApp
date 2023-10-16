package com.example.chatapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.StartActivity
import com.example.chatapplication.adapter.FollowingAdapter
import com.example.chatapplication.databinding.ActivityFollowingBinding
import com.example.chatapplication.dto.FollowingInfo
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FollowingActivity : AppCompatActivity() {

    private val binding: ActivityFollowingBinding by lazy {
        ActivityFollowingBinding.inflate(layoutInflater)
    }

    private val users = LoginActivity.users
    private val myInfo = MainActivity.myInfo

    private val followingAdapter by lazy {
        FollowingAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initView()
        bindView()

    }


    private fun initView() {

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            adapter = followingAdapter
        }

    }

    private fun bindView() {

        binding.closeButton.setOnClickListener {
            finish()
        }


        val userId = myInfo.userId
        val title = "following"

        users.child(userId).child(title)
            .addListenerForSingleValueEvent(object : ValueEventListener {
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


                        followingAdapter.items.add(
                            FollowingInfo(
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

                        followingAdapter.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

    }


}