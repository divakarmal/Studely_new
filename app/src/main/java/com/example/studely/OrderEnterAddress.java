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


public class OrderEnterAddress extends AppCompatActivity {
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home:
                        Intent home = new Intent(OrderEnterAddress.this,HomeLanding.class);
                        startActivity(home);
                        break;
                    case R.id.ic_myOrderList:
                        Intent orderList = new Intent(OrderEnterAddress.this,MyOrderList.class);
                        startActivity(orderList);
                        break;
                }
                return false;
            }
        });


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
