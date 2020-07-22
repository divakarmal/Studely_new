package com.example.studely.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.studely.DelivererConfirmation;
import com.example.studely.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d("NEW TOKEN", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification().getTitle().equals("Studely")) {
            String channelID = "Default";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody());
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            manager.notify(0, builder.build());
        } else {
            Log.d("NOTIF", "Loaded notif");
            Map<String, String> data = remoteMessage.getData();

            Bundle bundle = new Bundle();
            for (String key : data.keySet()) {
                bundle.putString(key, data.get(key));
            }
            Intent intent = new Intent(this, DelivererConfirmation.class);
            intent.putExtras(intent);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelID = "Default";
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle("Studely")
                    .setContentText(remoteMessage.getNotification().getBody())
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(channelID, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            manager.notify(0, builder.build());
        }
    }
}
