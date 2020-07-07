package com.example.studely;

import android.os.Bundle;

public class OrderPostingConfirmed extends BottomNavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_posting_confirmed);
        navBar(this.getApplicationContext());
    }
}
