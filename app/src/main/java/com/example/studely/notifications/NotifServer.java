package com.example.studely.notifications;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

public class NotifServer {
    public static void sendRegistrationTokenToServer() {

    }

    public static void sendMessage(String userTo, String message) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference notifRef = db.getReference().child("notificationRequests");

        String pushID = notifRef.push().getKey();

        try {
            JSONObject json = new JSONObject();
            JSONObject payload = new JSONObject();
            JSONObject notif = new JSONObject();
            notif.put("title", "Studely");
            notif.put("body", message);
            payload.put("notification", notif);
            json.put("userTo", userTo);
            json.put("payload", payload);

            Log.d("NOTIF", json.toString());
            notifRef.child(pushID).setValue(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendAwait(String userTo, String delivID) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference notifRef = db.getReference().child("notificationRequests");

        String pushID = notifRef.push().getKey();

        try {
            JSONObject json = new JSONObject();
            JSONObject payload = new JSONObject();
            JSONObject notif = new JSONObject();
            notif.put("title", "Studely");
            notif.put("body", "New delivery request");
            payload.put("notification", notif);
            JSONObject data = new JSONObject();
            data.put("pushID", delivID);
            payload.put("data", data);
            json.put("userTo", userTo);
            json.put("payload", payload);

            Log.d("NOTIF", json.toString());
            notifRef.child(pushID).setValue(json.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
