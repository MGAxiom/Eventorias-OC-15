package com.example.eventorias.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.eventorias.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class EventoriasFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveFcmToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        message.notification?.let { notification ->
            showNotification(
                title = notification.title ?: "Eventorias",
                body = notification.body ?: ""
            )
        }
    }

    private fun showNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Eventorias Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications pour les événements Eventorias"
        }
        notificationManager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo_eventorias_app_android)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun saveFcmToken(token: String) {
        android.util.Log.d("FCM", "New token: $token")
    }

    companion object {
        private const val CHANNEL_ID = "eventorias_notifications"
        private const val NOTIFICATION_ID = 1001
    }
}