package com.kartikasw.kelilinkseller.core.data.source.remote.service.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kartikasw.kelilinkseller.R
import com.kartikasw.kelilinkseller.core.data.helper.Constants.CHANNEL_ID
import com.kartikasw.kelilinkseller.core.data.helper.Constants.CHANNEL_NAME
import com.kartikasw.kelilinkseller.core.data.source.local.shared_pref.KelilinkPreference
import com.kartikasw.kelilinkseller.features.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class KelilinkFirebaseMessagingService: FirebaseMessagingService() {

    companion object {
        const val TAG = "FMService"

        private var sharedPref: KelilinkPreference? = null

        var token: String = ""
            set(value) {
                sharedPref?.setFcmToken(token)
                field = value
            }
    }

    override fun onNewToken(newToken: String) {
        Log.d(TAG, "Refreshed token: $token")
        token = newToken
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d(TAG, message.toString())
        sendNotification()
    }

    private fun sendNotification() {
        val contentIntent = Intent(applicationContext, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            contentIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )
        val notificationBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_notification)
            .setContentTitle("Kamu punya pesanan baru")
            .setContentText("Waktu terus berjalan, segera terima pesanan sebelum otomatis tertolak")
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationBuilder.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(Random.nextInt(), notificationBuilder.build())
    }

}