package com.example.drink

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        broadcaster = LocalBroadcastManager.getInstance(this)
    }

//Receiving notification foreground
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        handleMessage(remoteMessage)
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        //1
        val handler = Handler(Looper.getMainLooper())

        //2
        handler.post(Runnable {

            remoteMessage.notification?.let {
                val intent = Intent("MyData")
                intent.putExtra("message", it.body);
                broadcaster?.sendBroadcast(intent);
            }
            //Showing notification in toast while app open
            Toast.makeText(baseContext, getString(R.string.handle_notification_now),
                Toast.LENGTH_LONG).show()
        }
        )
    }



}

