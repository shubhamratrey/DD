package com.sillylife.dd.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.concurrent.atomic.AtomicInteger

class FirebaseMessaging : FirebaseMessagingService() {

    private val TAG = FirebaseMessaging::class.java.simpleName
    private val NOTIFICATION_ID = AtomicInteger(0)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.d(TAG, remoteMessage.data.toString() + "  " + remoteMessage.notification.toString())
        if (remoteMessage.data != null) {
            Log.d(TAG, remoteMessage.data.toString())
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d(TAG, s)
    }

}