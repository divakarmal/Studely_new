package com.example.studely.notifications;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotifServer {
    public static void sendRegistrationTokenToServer() {

    }

    public static void sendMessage(String userTo, String message) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference notifRef = db.getReference().child("notificationRequests");

        String pushID = notifRef.push().getKey();
        notifRef.child(pushID).child("userTo").setValue(userTo);
        notifRef.child(pushID).child("message").setValue(message);
    }
}
