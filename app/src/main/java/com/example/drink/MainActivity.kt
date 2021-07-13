package com.example.drink

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailabilityLight
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity() {
    lateinit var text_view_notification: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button_retrieve_token = findViewById<Button>(R.id.button_retrieve_token)
        text_view_notification = findViewById<TextView>(R.id.text_view_notification)

        button_retrieve_token.setOnClickListener(){
            FirebaseInstallations.getInstance().id.addOnCompleteListener(OnCompleteListener { task ->
                if(!task.isSuccessful){
                    Log.w("TAG", "getInstanceId Failed",task.exception)
                    return@OnCompleteListener
                }

                val token = FirebaseMessaging.getInstance().token

                val msg = getString(R.string.token_prefix, token)
                Log.d("TAG", msg)
                Toast.makeText(baseContext, msg, Toast.LENGTH_LONG).show()

            })
        }

        if (checkGooglePlayServices()) {

        } else {
            //You won't be able to send notifications to this device
            Log.w("TAG", "Device doesn't have google play services")
        }

        val bundle = intent.extras
        if (bundle != null) { //bundle must contain all info sent in "data" field of the notification
            text_view_notification.text = bundle.getString("text")
        }

    }



    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, IntentFilter("MyData"))

        //TODO: Register the receiver for notifications
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)

    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            text_view_notification.text = intent.extras?.getString("message")
        }
    }

    private fun checkGooglePlayServices(): Boolean{
        val status = GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(this)
        return if (status!= ConnectionResult.SUCCESS){
            Log.e("TAG","Error")
            false
        }else{
            Log.i("TAG","Google play services updated")
            true
        }
    }

}



