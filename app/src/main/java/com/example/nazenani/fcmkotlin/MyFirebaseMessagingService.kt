package com.example.nazenani.fcmkotlin

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    var fcmData: Map<String, String>? = null;
    var fcmNotification : RemoteMessage.Notification? = null;


    override fun onMessageReceived(remoteMessage : RemoteMessage) {
        var fcmFrom : String? = remoteMessage.from;
        if (remoteMessage.data.size > 0) {
            fcmData = remoteMessage.data;
        }
        if (remoteMessage.notification != null) {
            fcmNotification = remoteMessage.notification;
        }

        Log.d("data", fcmData.toString());
        Log.d("notification", fcmNotification.toString());
    }


    override fun onDeletedMessages() {
    }

}