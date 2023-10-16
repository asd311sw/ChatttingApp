package com.example.chatapplication.activity

import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapplication.R
import com.example.chatapplication.StartActivity
import com.example.chatapplication.databinding.ActivityProfileBinding
import com.google.android.material.chip.Chip
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private val binding: ActivityProfileBinding by lazy {
        ActivityProfileBinding.inflate(layoutInflater)
    }

    //private val otherUserId = intent.getStringExtra("userId").toString()
    private lateinit var database: FirebaseDatabase
    private lateinit var users:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        bindView()
    }

    private fun initView() {
        database = FirebaseDatabase.getInstance()
        users = database.getReference("users")

    }

    private fun bindView() {
        val otherUserId = intent.getStringExtra("userId").toString()
        val title = "profile"

        users.child(otherUserId).child(title).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for(data in snapshot.children){
                    val userInfo = data.value as Map<*,*>
                    val userName = userInfo.get("userName").toString()
                    val introduce = userInfo.get("introduce").toString()
                    val state = userInfo.get("state").toString()
                    val birthDate = userInfo.get("birthDate").toString()
                    val personality = userInfo.get("personality").toString()
                    val sex = userInfo.get("sex").toString()
                    val imageUri = Uri.parse(userInfo.get("imageUri").toString())
                    val storyCount = userInfo.get("storyCount").toString().toInt()
                    val followingCount = userInfo.get("followingCount").toString().toInt()
                    val followerCount = userInfo.get("followerCount").toString().toInt()


                    binding.apply {
                        imageView.setImageURI(imageUri)
                        userNameTextView.text = userName
                        birthDateTextView.text = birthDate
                        sexTextView.text = sex
                        introduceTextView.text = introduce
                        stateTextView.text = state
                    }


                    val personalities = personality.split(" ")
                    var result = ""
                    for(e in personalities){
                        result+=e+" "
                    }

                    binding.personalityTextView.text = result

//                    for (item in personalities) {
//                        val chip = Chip(applicationContext)
//                        chip.text = item
//                        chip.setChipBackgroundColorResource(R.color.gray)
//                        binding.personalityChipGroup.addView(chip)
//
//                    }


                }

            }

            override fun onCancelled(error: DatabaseError) {
            }


        })

        binding.closeButton.setOnClickListener {
            finish()
        }


    }


}