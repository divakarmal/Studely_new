package com.example.studely.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("NEW TOKEN", s);
    }
}
