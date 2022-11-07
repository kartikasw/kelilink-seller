package com.example.kelilinkseller.core.data.source.remote.service.notification

import android.util.Log
import com.example.kelilinkseller.core.data.source.local.shared_pref.KelilinkPreference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class KelilinkFirebaseMessagingService: FirebaseMessagingService() {

    companion object {
        const val TAG = "FMService"

        private var sharedPref: KelilinkPreference? = null

        var token: String? = null
            set(value) {
                sharedPref?.setFcmToken(token!!)
                field = value
            }
    }

    override fun onNewToken(newToken: String) {
        Log.d(TAG, "Refreshed token: $token")
        token = newToken
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")
        Log.d(TAG, "Message data payload: " + remoteMessage.data)
        Log.d(TAG, "Message Notification Body: ${remoteMessage.notification?.body}")
//        sendNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
    }

}