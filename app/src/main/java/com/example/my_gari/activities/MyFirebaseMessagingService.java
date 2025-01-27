package com.example.my_gari.activities;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG;

    public void onMessageReceived(RemoteMessage remoteMessage){
        Log.d(TAG, "From: "+remoteMessage.getFrom());

        if (remoteMessage.getData().size()>0){
            Log.d(TAG, "Message data payload: " + remoteMessage.getFrom());
        }

        if (remoteMessage.getNotification() != null){
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }
}
