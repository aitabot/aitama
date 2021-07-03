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
        Log.d("TAG", "The token refreshed: $token")
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        // super.onMessageReceived(me)
        Log.d(
            "MyFirebaseMessagingService",
            "Received Message: $message\ntitle:${message.notification?.title}\nbody: ${message.notification?.body}\n${message.data}"
        )

        passMessageToActivity(message)
    }

    private fun sendRegistrationToServer(token: String) {
        // TODO: ask user for a name instead of taking current time
        val nowUnixTime = System.currentTimeMillis().toString()

        val requestQueue = RequestQueueSingleton.getInstance(applicationContext)
        val request =
            buildRequest(fcmToken = token, userID = nowUnixTime, context = applicationContext)

        requestQueue.addToRequestQueue(request)
    }

    private fun buildRequest(
        fcmToken: String,
        userID: String,
        context: Context
    ): JsonObjectRequest {

        val builder: Uri.Builder = Uri.Builder()
        builder.scheme("http")
            .encodedAuthority("aitamonolith.qtq.at")
            .appendPath("notification")
            .appendPath("register")

        val url: String = builder.build().toString()

        /* Prepare JSON Request Body*/
        val params: MutableMap<String, String> = HashMap()
        params["user_id"] = userID
        params["fcm_token"] = fcmToken
        val jsonBody = JSONObject(params as Map<*, *>)

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(context, "Token requested", Toast.LENGTH_SHORT).show()
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
        super.onMessageReceived(message);

        val intent = Intent().apply {
            action = INTENT_ACTION_SEND_MESSAGE
            putExtra("action", message.data["action"])
            putExtra("asset", message.data["asset"])
            putExtra("amount", message.data["amount"])
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        Log.d("MyFirebaseMessagingService", "Send Broadcast Intent")

    }

    companion object {
        const val INTENT_ACTION_SEND_MESSAGE = "INTENT_ACTION_SEND_MESSAGE"
    }
}