package com.example.chatapplication

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.SharedMemory
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.chatapplication.activity.ChatActivity
import com.example.chatapplication.dto.ChatInfo
import com.example.chatapplication.dto.UserInfo
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UploadWorker(appContext: Context, workerParameters: WorkerParameters) :
    Worker(appContext, workerParameters) {

    private val notificationManager by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    //private lateinit var userId:String
    private lateinit var otherUserId:String
    private lateinit var database:FirebaseDatabase
    private lateinit var users:DatabaseReference
   // private lateinit var shared:SharedPreferences

    override fun doWork(): Result {
        initView()
        bindView()


        return Result.success()
    }

    private fun initView(){
        database = FirebaseDatabase.getInstance()
        users = database.getReference("users")
        //shared = applicationContext.getSharedPreferences("userId",Activity.MODE_PRIVATE)
        //userId = shared.getString("userId","null").toString()
    }

    private fun bindView(){

        val title = "notification"

        users.child(title).addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatInfo = snapshot.value as Map<*,*>
                val otherUserId = chatInfo.get("userId").toString()
                val message = chatInfo.get("message").toString()
                val currentTime = chatInfo.get("currentTime").toString()

//                if(otherUserId != userId) {
//                    createNotification(ChatInfo(otherUserId, message, currentTime))
//
//                    MainActivity.badgeDrawable.apply {
//                        isVisible = true
//                        backgroundColor = applicationContext.getColor(R.color.red)
//                    }
//                }

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

        //users.child(userId).child(title).removeValue()
    }



    private fun createNotification(chatInfo: ChatInfo) {
        otherUserId = chatInfo.userId
        val title = "profile"

        users.child(otherUserId).child(title).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val userInfo = data.value as Map<*,*>
                    val otherUserName = userInfo.get("userName").toString()


                    val channelId = "WorkManager"
                    val channelDescription = ""
                    val importance = NotificationManager.IMPORTANCE_DEFAULT

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                        val channel = NotificationChannel(channelId, channelDescription, importance)
                        notificationManager.createNotificationChannel(channel)
                        val bitmap = BitmapFactory.decodeResource(
                            applicationContext.getResources(),
                            R.drawable.zombie_icon
                        )


                        val notification = NotificationCompat.Builder(applicationContext, channelId)
                            .setSmallIcon(R.drawable.google_g_icon)
                            .setLargeIcon(bitmap)
                            .setContentTitle("😚${otherUserName}으로부터 새로운 메시지가 도착했습니다!😚")
                            .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                            .setStyle(
                                NotificationCompat.BigTextStyle()
                                    .bigText(chatInfo.message)
                            )
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(getContentIntent())
                            .setAutoCancel(true)
                            .build()


                        if (ActivityCompat.checkSelfPermission(
                                applicationContext,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return
                        }


                        notificationManager.notify(1010, notification)

                }
            }

            }

            override fun onCancelled(error: DatabaseError) {
            }


        })
    }

    private fun getContentIntent(): PendingIntent {
        val intent = Intent(applicationContext, ChatActivity::class.java).apply {
            putExtra("userId", otherUserId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_MUTABLE)

        return pendingIntent
    }


}

