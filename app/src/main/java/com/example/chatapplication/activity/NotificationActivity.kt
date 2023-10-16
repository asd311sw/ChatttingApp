package com.example.chatapplication.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.StartActivity
import com.example.chatapplication.adapter.NotificationAdapter
import com.example.chatapplication.databinding.ActivityNotificationBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError

class NotificationActivity:AppCompatActivity() {

    private val binding:ActivityNotificationBinding by lazy {
        ActivityNotificationBinding.inflate(layoutInflater)
    }

    private val notificationAdapter by lazy {
        NotificationAdapter()
    }

    private val users = LoginActivity.users
    private val myInfo = MainActivity.myInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        bindView()
    }

    private fun initView(){

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext,RecyclerView.VERTICAL,false)
            adapter = notificationAdapter
        }



    }

    private fun bindView(){

        binding.closeButton.setOnClickListener {
            finish()
        }

        val userId = myInfo.userId
        val title = "notification"

        users.child(userId).child(title).addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

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



}