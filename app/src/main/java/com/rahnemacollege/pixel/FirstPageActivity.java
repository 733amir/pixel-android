package com.rahnemacollege.pixel;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class FirstPageActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentClickHandler,
        SignUpFragment.SignUpFragmentClickHandler {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter firstPageFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.first_page_title);

        viewPager = findViewById(R.id.first_page_container);
        viewPager.setOffscreenPageLimit(2);
        tabLayout = findViewById(R.id.first_page_tab);
        firstPageFragmentAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        firstPageFragmentAdapter.addFragment(new LoginFragment(), getString(R.string.firstpage_b_login));
        firstPageFragmentAdapter.addFragment(new SignUpFragment(), getString(R.string.firstpage_b_signup));

        viewPager.setAdapter(firstPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).select();
    }

    public void forgetPasswordClicked() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    public void loginClicked(String username, String password) {
        // TODO Connect to server and check username and password.

        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_down, R.anim.slide_to_up);
    }

    public void signUpClicked(String fullname, String username, String password, String email) {
        tabLayout.getTabAt(0).select();
        ((LoginFragment) firstPageFragmentAdapter.getItem(0)).fillUsernamePassword(username, password);
    }
}