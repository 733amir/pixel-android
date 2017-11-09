package com.rahnemacollege.pixel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);
    }

    public void onLogin(View view) {
        Intent toLogin = new Intent(this, LoginActivity.class);
        startActivity(toLogin);
    }

    public void onSignup(View view) {
        Intent toSignup = new Intent(this, SignupActivity.class);
        startActivity(toSignup);
    }
}
