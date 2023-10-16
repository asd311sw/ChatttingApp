package com.example.chatapplication.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapplication.MainActivity
import com.example.chatapplication.R
import com.example.chatapplication.StartActivity
import com.example.chatapplication.UploadWorker
import com.example.chatapplication.adapter.ChatAdapter
import com.example.chatapplication.databinding.ActivityChatBinding
import com.example.chatapplication.dto.ChatInfo
import com.example.chatapplication.fragment.ConversFragment
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date

class ChatActivity : AppCompatActivity() {

    companion object {
        var otherUserId: String = ""
    }

    private val binding: ActivityChatBinding by lazy {
        ActivityChatBinding.inflate(layoutInflater)
    }


    private val users = LoginActivity.users
    private val myInfo = LoginActivity.myInfo
    private var lastChatContent = ""
    private var lastChatTime = ""

    private val chatAdapter by lazy {
        ChatAdapter()
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
            adapter = chatAdapter
        }

//        binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
//
//        binding.scrollView.post(object :Runnable{
//            override fun run() {
//
//            }
//
//
//        })

    }


    private fun bindView() {

        binding.closeButton.setOnClickListener {
            finish()
        }
//
//        binding.scrollView.post(object :Runnable{
//            override fun run() {
//
//                binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
//            }
//
//
//        })


        otherUserId = intent.getStringExtra("userId").toString()
        val title = "chat"


        binding.sendButton.setOnClickListener {
            val content = binding.inputEditText.text.toString()
            val sdf = SimpleDateFormat("yyyy.MM.dd hh:mm")
            val currentTime = sdf.format(Date())

            lastChatContent = content
            lastChatTime = currentTime

            binding.inputEditText.setText("")

            LoginActivity.kakaoCurrentUser?.let {
                val userId = it.id.toString()
                users.child(userId).child(title).child(otherUserId).push()
                    .setValue(ChatInfo(userId, content, currentTime))
                users.child(otherUserId).child(title).child(userId).push()
                    .setValue(ChatInfo(userId, content, currentTime))
            }

            //구글 로그인
            LoginActivity.googleCurrentUser?.let {
                val userId = it.id.toString()
                users.child(userId).child(title).child(otherUserId).push()
                    .setValue(ChatInfo(userId, content, currentTime))
                users.child(otherUserId).child(title).child(userId).push()
                    .setValue(ChatInfo(userId, content, currentTime))

            }

          //  binding.scrollView.fullScroll(View.FOCUS_DOWN);
        }

//
//        users.child(myInfo.userId).child(title).child(otherUserId)
//            .addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (data in snapshot.children) {
//
//                        val chatInfo = data.value as Map<*, *>
//                        val userId = chatInfo.get("userId").toString()
//                        val message = chatInfo.get("message").toString()
//                        val currentTime = chatInfo.get("currentTime").toString()
//
//                        chatAdapter.items.add(ChatInfo(userId,message,currentTime))
//                        chatAdapter.notifyDataSetChanged()
//
//                    }
//
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                }
//
//            })
//

        users.child(myInfo.userId).child(title).child(otherUserId).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                updateData(snapshot)
                //binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
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

    private fun updateData(snapshot: DataSnapshot) {

        val value = (snapshot.value as Map<*, *>)
        val chatInfo = value
        val currentTime = chatInfo.get("currentTime").toString()
        val message = chatInfo.get("message").toString()
        val userId = chatInfo.get("userId").toString()


        chatAdapter.items.add(ChatInfo(userId, message, currentTime))
        chatAdapter.notifyDataSetChanged()

        scrollToEnd()




    }


    private fun scrollToEnd(){

        binding.recyclerView.scrollToPosition(chatAdapter.items.size - 1)

    }

//
//    private fun changeFragment(fragment: Fragment) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.fragmentContainerView, fragment)
//            .commit()
//    }

//    override fun onBackPressed() {
//        super.onBackPressed()
//
//        val conversFragment = ConversFragment()
//        conversFragment.sendData(otherUserId, lastChatContent, lastChatTime)
//
//        changeFragment(conversFragment)
//
//    }


}