package com.rahnemacollege.pixel;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.BottomNavigationView;

public class FirstPage extends FragmentActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        ViewPager viewPager = findViewById(R.id.first_page_container);
        TabLayout tabLayout = findViewById(R.id.first_page_tab);
        FirstPageFragmentAdapter firstPageFragmentAdapter = new FirstPageFragmentAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(firstPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(1).select();
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
