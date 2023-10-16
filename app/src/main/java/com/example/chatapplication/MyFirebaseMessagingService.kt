package com.example.chatapplication

import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {

    init {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (!it.isSuccessful) {
                Log.i("FAILED", "Fetching FCM registration token failed", it.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = it.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
           // Log.i("FCM", msg)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)


    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

}