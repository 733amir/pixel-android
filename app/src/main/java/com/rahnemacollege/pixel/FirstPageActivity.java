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

        // Customize actionBar layout
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.first_page_title);

        // Config fragmentPagerAdapter of FirstPage
        firstPageFragmentAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        firstPageFragmentAdapter.addFragment(new LoginFragment(), getString(R.string.firstpage_b_login));
        firstPageFragmentAdapter.addFragment(new SignUpFragment(), getString(R.string.firstpage_b_signup));

        // Config viewPager of FirstPage
        viewPager = findViewById(R.id.first_page_container);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(firstPageFragmentAdapter);

        // Config tabLayout of FirstPage
        tabLayout = findViewById(R.id.first_page_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();
    }

    /**
     * When ForgetPassword TextView on LoginFragment is selected this method is called.
     */
    public void forgetPasswordClicked() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * When Login button is selected this method connect to server and verify the information and
     * then switch to user explore activity.
     *
     * @param username Username that user wrote.
     * @param password Password that user wrote.
     */
    public void loginClicked(String username, String password) {
        // TODO Connect to server and check username and password.

        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_down, R.anim.slide_to_up);
    }

    /**
     * When SignUp button in SignUpFragment is clicked this method gets called and register user on
     * the server.
     *
     * @param fullname Fullname user inputed.
     * @param username Username user inputed.
     * @param password Password user inputed.
     * @param email Email user inputed.
     */
    public void signUpClicked(String fullname, String username, String password, String email) {
        tabLayout.getTabAt(0).select();
        ((LoginFragment) firstPageFragmentAdapter.getItem(0)).fillUsernamePassword(username, password);
    }

}