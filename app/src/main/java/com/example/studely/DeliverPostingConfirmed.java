package com.example.studely;

import android.os.Bundle;

public class DeliverPostingConfirmed extends BottomNavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_post_confirm);
        navBar(this.getApplicationContext());
    }
}
