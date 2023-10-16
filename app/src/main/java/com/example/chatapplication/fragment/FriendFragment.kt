package com.example.chatapplication.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.StartActivity
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.adapter.FriendAdapter
import com.example.chatapplication.databinding.FragmentFriendBinding
import com.example.chatapplication.dto.FriendInfo
import com.example.chatapplication.dto.UserInfo
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FriendFragment : Fragment() {

    private val binding: FragmentFriendBinding by lazy {
        FragmentFriendBinding.inflate(layoutInflater)
    }


    private lateinit var userId:String
    private val users = LoginActivity.users
    private lateinit var myInfo:UserInfo

    private val friendAdapter by lazy {
        FriendAdapter()
    }

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
        LoginActivity.kakaoCurrentUser?.let {
            userId = it.id.toString()
        }


        LoginActivity.googleCurrentUser?.let {
            userId = it.id.toString()
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = friendAdapter
        }
    }

    private fun bindView() {

        getMyUserInfo()
        getAllUserInfo()
    }


    private fun getMyUserInfo(){

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


                binding.apply {
                    imageView.setImageURI(Uri.parse(imageUri))
                    stateTextView.text = state
                }

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

    private fun getAllUserInfo(){

        users.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val userIds = data.key.toString()


                    if(userIds != "isVaildUser" && userIds != userId && userIds != "notification") {
                        users.child(userIds).child("profile").get().addOnCompleteListener {
                            if (it.isComplete) {
                                val result = it.result.value
                                val userInfo = (result as Map<*, *>).values.first() as Map<*, *>
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

                                val info = FriendInfo(
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


                                friendAdapter.items.add(info)
                                friendAdapter.notifyDataSetChanged()

                            }

                        }
                    }



                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FAILED","Cancelled")
            }


        })
    }
//
//    private fun setUserInfo(userInfo: UserInfo) {
//
//
//        LoginActivity.kakaoCurrentUser?.let {
//            if (it.id.toString() == userInfo.userId) {
//                binding.apply {
//                    imageView.setImageURI(Uri.parse(userInfo.imageUri))
//                    stateTextView.text = userInfo.state
//                }
//            } else {
//                friendAdapter.items.add(
//                    FriendInfo(
//                        userInfo.userId,
//                        userInfo.userName,
//                        userInfo.introduce,
//                        userInfo.state,
//                        userInfo.birthDate,
//                        userInfo.personality,
//                        userInfo.sex,
//                        Uri.parse(userInfo.imageUri)
//                    )
//                )
//
//                friendAdapter.notifyDataSetChanged()
//
//            }
//        }
//
//
//        LoginActivity.googleCurrentUser?.let {
//            if (it.id.toString() == userInfo.userId) {
//                binding.imageView.setImageURI(Uri.parse(userInfo.imageUri))
//                binding.stateTextView.text = userInfo.state
//            } else {
//                friendAdapter.items.add(
//                    FriendInfo(
//                        userInfo.userId,
//                        userInfo.userName,
//                        userInfo.introduce,
//                        userInfo.state,
//                        userInfo.birthDate,
//                        userInfo.personality,
//                        userInfo.sex,
//                        Uri.parse(userInfo.imageUri)
//                    )
//                )
//                friendAdapter.notifyDataSetChanged()
//            }
//        }
//    }


}
