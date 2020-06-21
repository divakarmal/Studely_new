package com.example.studely;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class OrderEnterAddress extends BottomNavBar {
    EditText address;
    CheckBox priAddBool;
    Button mNextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_enter_address);

        address = findViewById(R.id.addressField);
        mNextBtn = findViewById(R.id.NextBtn);
        priAddBool = findViewById(R.id.priAddCheck);

        navBar(this.getApplicationContext());


        mNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(getApplicationContext(), OrderCanteenSelect.class);
                if(priAddBool.isChecked()){
                    //TODO
                } else {
                    String add = address.getText().toString();
                    newIntent.putExtra("orderDestination", add);
                }
                startActivity(newIntent);
            }
        });

    }
}
