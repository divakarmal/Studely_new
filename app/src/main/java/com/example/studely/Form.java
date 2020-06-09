package com.example.studely;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class Form extends AppCompatActivity {

    EditText field1, field2, field3;
    Button mSubmitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        field1 = findViewById(R.id.field1);
        field2 = findViewById(R.id.field2);
        field3 = findViewById(R.id.field3);
        mSubmitBtn = findViewById(R.id.SubmitBtn);

        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text1, text2, text3;
                text1 = String.valueOf(field1.getText());
                text2 = String.valueOf(field2.getText());
                text3 = String.valueOf(field3.getText());

                JSONObject sendData = new JSONObject();
                try {
                    sendData.put("Val1", text1);
                    sendData.put("Val2", text2);
                    sendData.put("Val2", text3);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pushToDB(sendData);
            }
        });
    }

    private void pushToDB(JSONObject jsonObj) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            return;
        }
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference userRef = fireDB.getReference("users");

        JSONObject send = new JSONObject();
        try {
            send.put(uid, jsonObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        userRef.setValue(send);
    }
}
