package com.example.chatapplication

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.example.chatapplication.activity.LoginActivity
import com.example.chatapplication.activity.NotificationActivity
import com.example.chatapplication.databinding.ActivityMainBinding
import com.example.chatapplication.databinding.ItemChatBinding
import com.example.chatapplication.dto.UserInfo
import com.example.chatapplication.fragment.ConversFragment
import com.example.chatapplication.fragment.FriendFragment
import com.example.chatapplication.fragment.ProfileFragment
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.badge.BadgeUtils
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val users = LoginActivity.users
    private lateinit var userId:String

    companion object {
        lateinit var badgeDrawable: BadgeDrawable
        lateinit var myInfo: UserInfo
    }

    init {

        getMyUserInfo()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getMyUserInfo()
        initView()
        bindView()
    }

    private fun getMyUserInfo(){
        LoginActivity.kakaoCurrentUser?.let {
            userId = it.id.toString()
        }


        LoginActivity.googleCurrentUser?.let {
            userId = it.id.toString()
        }

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

               // setWorkRequest()

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

    private fun initView() {


        setSupportActionBar(binding.toolBar)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolBar,
            R.string.open,
            R.string.close
        )

        toggle.drawerArrowDrawable.color = getColor(R.color.white)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


    }

    private fun bindView() {




        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.friend -> {
                    changeFragment(FriendFragment())
                    binding.toolBar.title = "친구목록"
                }

                R.id.conversation -> {

                    changeFragment(ConversFragment())
                    binding.toolBar.title = "대화목록"
                }

                R.id.myPage -> {

                    changeFragment(ProfileFragment())
                    binding.toolBar.title = "내 정보"
                }

            }

            return@setOnItemSelectedListener true
        }




        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {


            }

            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

    }


    private fun setWorkRequest() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(binding.root.context).enqueue(workRequest)
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView, fragment)
            .commit()

    }

    @androidx.annotation.OptIn(com.google.android.material.badge.ExperimentalBadgeUtils::class)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)

        badgeDrawable = BadgeDrawable.create(this).apply {
            isVisible = false
            backgroundColor = getColor(R.color.red)
        }

        BadgeUtils.attachBadgeDrawable(badgeDrawable, binding.toolBar, R.id.notification)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {

            }

            R.id.notification -> {

                val intent = Intent(this, NotificationActivity::class.java)
                startActivity(intent)

            }

        }

        return true
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else
            super.onBackPressed()
    }


}

