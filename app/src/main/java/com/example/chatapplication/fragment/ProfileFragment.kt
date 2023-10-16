package com.example.chatapplication.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.Capability
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.StartActivity
import com.example.chatapplication.activity.FollowerActivity
import com.example.chatapplication.activity.FollowingActivity
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.adapter.StoryAdapter
import com.example.chatapplication.databinding.FragmentConversBinding
import com.example.chatapplication.databinding.FragmentProfileBinding
import com.example.chatapplication.dto.StoryInfo
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class ProfileFragment:Fragment() {
    private val binding: FragmentProfileBinding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private val users = LoginActivity.users
    private val myInfo = LoginActivity.myInfo
    private var storyCount = myInfo.storyCount
    private var followingCount = myInfo.followingCount
    private var followerCount = myInfo.followerCount

    private val storyAdapter by lazy {
        StoryAdapter()
    }


    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            binding.imageView.setImageURI(uri)
            updateProfile(uri)

            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }


    val pickMultipleMedia =
        registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(5)) { uris ->
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (uris.isNotEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: ${uris.size}")

                //Toast.makeText(requireContext(),"${uris.size}",Toast.LENGTH_SHORT).show()
                //storyCount = shared.getInt("storyCount",0)
                storyCount += uris.size
                myInfo.storyCount = storyCount
                binding.postCountTextView.text = storyCount.toString()
//                with(shared.edit()){
//                    putInt("storyCount",storyCount)
//                    commit()
//                }

                users.child(myInfo.userId).child("profile").removeValue()
                users.child(myInfo.userId).child("profile").push().setValue(myInfo)

                for(uri in uris){
                    val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    requireContext().contentResolver.takePersistableUriPermission(uri, flag)

                    val storyInfo = StoryInfo(uri.toString())

                    storyAdapter.items.add(storyInfo)
                    storyAdapter.notifyDataSetChanged()

                    users.child(myInfo.userId).child("story").push().setValue(storyInfo)
                }

               // Toast.makeText(requireContext(),"${storyAdapter.items.size}",Toast.LENGTH_SHORT).show()

            } else {
                Log.d("PhotoPicker", "No media selected")
            }
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



    private fun initView(){
        //shared = requireContext().getSharedPreferences("story", Activity.MODE_PRIVATE)
        //storyCount = shared.getInt("storyCount",0)
        binding.apply {
            postCountTextView.text = storyCount.toString()
            followingCountTextView.text = followingCount.toString()
            followerCountTextView.text = followerCount.toString()

            imageView.setImageURI(Uri.parse(myInfo.imageUri))
            userNameTextView.text = myInfo.userName
            recyclerView.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                adapter = storyAdapter
            }
        }


    }


    private fun bindView(){
        getMyStoryInfo()

        binding.imageView.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        binding.followingCountTextView.setOnClickListener {
            val intent = Intent(requireContext(),FollowingActivity::class.java)
            startActivity(intent)
        }

        binding.followerCountTextView.setOnClickListener {
            val intent = Intent(requireContext(),FollowerActivity::class.java)
            startActivity(intent)
        }

        binding.addStoryButton.setOnClickListener {
            pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
        }
    }

    private fun updateProfile(uri:Uri){
        val title = "profile"
        users.child(myInfo.userId).child(title).removeValue()
        myInfo.imageUri = uri.toString()
        users.child(myInfo.userId).child(title).push().setValue(myInfo)
    }

    private fun getMyStoryInfo(){

        users.child(myInfo.userId).child("story").addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val storyInfo = data.value as Map<*,*>
                    val imageUri = storyInfo.get("imageUri").toString()

                    storyAdapter.items.add(StoryInfo(imageUri))
                    storyAdapter.notifyDataSetChanged()

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FAILURE","Cancelled")
            }


        })
//
//        users.child(myInfo.userId).child("story").addChildEventListener(object :ChildEventListener{
//            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
//
//            }
//
//            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
//            }
//
//            override fun onChildRemoved(snapshot: DataSnapshot) {
//            }
//
//            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//
//        })


    }

}