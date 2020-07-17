package com.example.studely.notifications;

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
            JSONObject notif = new JSONObject();
            notif.put("title", "Studely");
            notif.put("body", message);
            json.put("notification", notif);

            notifRef.child(pushID).setValue(json);
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
            JSONObject notif = new JSONObject();
            notif.put("title", "Studely");
            notif.put("body", "New delivery request");
            json.put("notification", notif);
            JSONObject data = new JSONObject();
            data.put("pushID", delivID);
            json.put("data", data);
            notifRef.child(pushID).setValue(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
