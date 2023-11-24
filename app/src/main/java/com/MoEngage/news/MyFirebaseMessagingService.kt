package com.MoEngage.news

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.MoEngage.news.model.Article
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    val notificationId = 1
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)


        // Check if the message contains data payload
        remoteMessage.data.isNotEmpty().let {
            // Extract data from the payload
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]

            // Handle the received data
            title?.let { articleTitle ->
                message?.let { articleMessage ->
                    // Display notification
                    displayNotification(articleTitle, articleMessage)
                }
            }
            Log.d("FCM", "Title: $title, Message: $message")
        }
    }

    private fun displayNotification(title: String, message: String) {
        val channelId = "your_channel_id"
        val channelName = "Your Channel Name"

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.news)
            .setAutoCancel(true)

        val notificationId = /* Unique notification ID */
            notificationManager.notify(notificationId, notificationBuilder.build())
    }

    override fun onNewToken(token: String) {
        // Called when a new token is generated
        // Log or use the token as needed
        Log.d("FCM Token", token)

        // Here, you can send this token to your server for targeted notifications
    }






}