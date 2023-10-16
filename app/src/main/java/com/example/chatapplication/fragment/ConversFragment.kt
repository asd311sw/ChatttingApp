package com.example.chatapplication.fragment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.StartActivity
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.adapter.RoomAdapter
import com.example.chatapplication.databinding.FragmentConversBinding
import com.example.chatapplication.dto.RoomInfo
import com.example.chatapplication.dto.UserInfo
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class ConversFragment : Fragment() {
    private val binding: FragmentConversBinding by lazy {
        FragmentConversBinding.inflate(layoutInflater)
    }

    private val bundle by lazy {
        Bundle()
    }

    private val roomAdapter by lazy {
        RoomAdapter()
    }

    private val users = LoginActivity.users
    private val myInfo = LoginActivity.myInfo


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        bindView()
    }


    private fun initView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = roomAdapter
        }
        Log.d("CHECK","CHECKKCKCKCKCKC")

    }

    private fun bindView() {
        val userId = myInfo.userId
        val title = "chat"



        users.child(userId).child(title).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val otherUserId = snapshot.key.toString()

                val lastInfo = (snapshot.value as Map<*, *>).values.sortedBy {
                    val item = it as Map<*,*>
                    item.get("currentTime").toString()
                }

                val lastChatInfo = lastInfo.last() as Map<*,*>
                val lastChatContent = lastChatInfo.get("message").toString()
                val lastChatTime = lastChatInfo.get("currentTime").toString()

                users.child(otherUserId).child("profile").get().addOnCompleteListener {

                    if (it.isComplete) {
                        val result = it.result.value
                        val userInfo = (result as Map<*, *>).values.first() as Map<*, *>
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

                        val otherUserInfo = UserInfo(
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

                        //val lastUser = UserInfo(lastUserId,userName,introduce,state,birthDate,personality,sex,imageUri)


                        roomAdapter.items.add(
                            RoomInfo(
                                otherUserId,
                                otherUserInfo,
                                lastChatContent,
                                lastChatTime
                            )
                        )


                        roomAdapter.notifyDataSetChanged()
                    }

                }
                //val lastUserInfo = (users.child(lastUserId).child("profile").get().result.value as Map<*,*>).values.first() as Map<*,*>


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
//    fun sendData(userId: String, lastChatContent: String, lastChatTime: String) {
//
//
//        bundle.apply {
//            putString("userId", userId)
//            putString("lastChatContent", lastChatContent)
//            putString("lastChatTime", lastChatTime)
//        }
//
//
//    }

}