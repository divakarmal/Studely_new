package com.example.studely;

import android.content.Intent;
import android.os.Bundle;

public class DeliverPostingConfirmed extends BottomNavBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliver_post_confirm);
        navBar(this.getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DeliverPostingConfirmed.this, HomeLanding.class);
        startActivity(intent);
    }
}
