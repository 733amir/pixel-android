package com.rahnemacollege.pixel;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class FirstPage extends AppCompatActivity
        implements LoginFragment.LoginFragmentClickHandler,
        SignUpFragment.SignUpFragmentClickHandler {

    TabLayout tabLayout;
    ViewPager viewPager;
    FirstPageFragmentAdapter firstPageFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        viewPager = findViewById(R.id.first_page_container);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.first_page_tab);
        firstPageFragmentAdapter = new FirstPageFragmentAdapter(this, getSupportFragmentManager());

        viewPager.setAdapter(firstPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).select();
    }

    public void forgetPasswordClicked() {
//        tabLayout.getTabAt(0).select();

        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void loginClicked(String username, String password) {
        // TODO Connect to server and check username and password.

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_down, R.anim.slide_to_up);
    }

    public void signUpClicked(String fullname, String username, String password, String email) {
        tabLayout.getTabAt(1).select();
        ((LoginFragment) firstPageFragmentAdapter.getItem(1)).fillUsernamePassword(username, password);
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