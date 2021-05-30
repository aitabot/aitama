package com.example.aitama

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // super.onNewToken(token)
        Log.d("TAG", "The token refreshed: $token")


    }

    override fun onMessageReceived(message: RemoteMessage) {
        // super.onMessageReceived(p0)
        Log.d("TAG", "Received Message: $message\ntitle:${message.notification?.title}\nbody: ${message.notification?.body}\n${message.data}")

        passMessageToActivity(message)
    }

    fun sendRegistrationToServer(token: String) {
        // TODO: ask user for a name instead of taking current time
        val nowUnixTime = System.currentTimeMillis().toString()

        // TODO: Send request to server
        // POST http://localhost:8000/notification/register
        // {
        //   "userID": nowUnixTime,
        //   "fcmToken": token
        // }

    }

    private fun passMessageToActivity(message: RemoteMessage) {

        val intent = Intent().apply{
            action = INTENT_ACTION_SEND_MESSAGE
            putExtra("message", message)
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

    }

    companion object {
        const val INTENT_ACTION_SEND_MESSAGE = "INTENT_ACTION_SEND_MESSAGE"
    }
}