package com.example.aitama

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.aitama.rest.RequestQueueSingleton
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // super.onNewToken(token)
        Log.d("TAG", "The token refreshed: $token")
        sendRegistrationToServer(token)

    }

    override fun onMessageReceived(message: RemoteMessage) {
        // super.onMessageReceived(p0)
        Log.d(
            "TAG",
            "Received Message: $message\ntitle:${message.notification?.title}\nbody: ${message.notification?.body}\n${message.data}"
        )

        passMessageToActivity(message)
    }

    private fun sendRegistrationToServer(token: String) {
        // TODO: ask user for a name instead of taking current time
        val nowUnixTime = System.currentTimeMillis().toString()

        val requestQueue = RequestQueueSingleton.getInstance(applicationContext)
        val request =
            buildRequest(fcmToken = token, userID = "1", context = applicationContext)
        requestQueue.addToRequestQueue(request)


        // TODO: Send request to server
        // POST http://localhost:8000/notification/register
        // {
        //   "userID": nowUnixTime,
        //   "fcmToken": token
        // }

    }

    private fun buildRequest(
        fcmToken: String,
        userID: String,
        context: Context
    ): JsonObjectRequest {

        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("http")
            .encodedAuthority("10.0.2.2:8000")
            .appendPath("notification")
            .appendPath("register")

        val url: String = builder.build().toString()

        /* Prepare JSON Request Body*/
        val params: MutableMap<String, String> = HashMap()
        params["userID"] = userID
        params["fcmToken"] = fcmToken
        val jsonBody = JSONObject(params as Map<*, *>)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(context, "Token requested", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(
                    context,
                    "Token request failed: $error",
                    Toast.LENGTH_LONG
                ).show()
            }
        )
        return jsonObjectRequest
    }


    private fun passMessageToActivity(message: RemoteMessage) {

        val intent = Intent().apply {
            action = INTENT_ACTION_SEND_MESSAGE
            putExtra("message", message)
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

    }

    companion object {
        const val INTENT_ACTION_SEND_MESSAGE = "INTENT_ACTION_SEND_MESSAGE"
    }
}