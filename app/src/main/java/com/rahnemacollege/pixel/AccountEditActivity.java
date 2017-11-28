package com.rahnemacollege.pixel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class AccountEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        getSupportActionBar().setTitle(R.string.account_title);

    }
}
