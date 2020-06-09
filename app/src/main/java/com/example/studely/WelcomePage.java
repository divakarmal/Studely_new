package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomePage extends AppCompatActivity {

    TextView textView2;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        nextBtn = findViewById(R.id.nextBtn);
        textView2 = findViewById(R.id.textView2);

        getFromDB();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Form.class));
            }
        });
    }

    private void getFromDB() {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            return;
        }
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference userRef = fireDB.getReference("users");
        DatabaseReference childRef = userRef.child(uid);

        Log.i("DATABASE UID", uid);
        childRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String out = dataSnapshot.getValue(String.class);
                Log.i("DATABASE SUCCESS", out);
                textView2.setText(out);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("DATABASE FAIL", "failed to read", databaseError.toException());
            }
        });
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }
}
