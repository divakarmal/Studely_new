package com.example.studely;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ImageButton proceed;
    Button logRegBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logRegBtn = findViewById(R.id.loginRegBtn);
        proceed = findViewById(R.id.nextmg);

        String msg = getIntent().getStringExtra("Toast");
        if (msg != null) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.d("AUTH", "User is logged in");
                    proceed.setVisibility(View.VISIBLE);
                } else {
                    Log.d("AUTH", "User is not logged in");
                    logRegBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        logRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeLanding.class));
            }
        });


    }
}
