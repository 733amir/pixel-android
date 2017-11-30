package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class ExploreActivity extends AppCompatActivity
        implements ProfileFragment.ProfileClickHandler {

    String TAG = "ExploreActivity";

    HomeFragment homeFragment;
    FriendsListFragment friendsListFragment;
    ProfileFragment profileFragment;

    MyFragmentPagerAdapter explorePageFragmentAdapter;
    ViewPager contentViewPager;
    BottomNavigationView bottomNavigationView;

    SharedPreferences sharedPref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean status = false;
            switch (item.getItemId()) {
                case R.id.home_nav_explore:
                    contentViewPager.setCurrentItem(0);
                    status = true;
                    break;
                case R.id.home_nav_friends_list:
                    contentViewPager.setCurrentItem(1);
                    status = true;
                    break;
                case R.id.home_nav_me:
                    contentViewPager.setCurrentItem(2);
                    status = true;
                    break;
            }
            return status;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        // Create explore activity fragments
        homeFragment = new HomeFragment();
        friendsListFragment = new FriendsListFragment();
        profileFragment = new ProfileFragment();

        // Create and config explore explorePageFragmentAdapter
        explorePageFragmentAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        explorePageFragmentAdapter.addFragment(homeFragment, getString(R.string.explore_nav_home));
        explorePageFragmentAdapter.addFragment(friendsListFragment, getString(R.string.explore_nav_friends_list));
        explorePageFragmentAdapter.addFragment(profileFragment, getString(R.string.explore_nav_me));

        // Config viewPager of explore activity
        contentViewPager = findViewById(R.id.explore_container);
        contentViewPager.setOffscreenPageLimit(2);
        contentViewPager.setAdapter(explorePageFragmentAdapter);
        contentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        // Config bottomNavigationView
        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.home_nav_me);

        // Shared Preferences
        sharedPref = getSharedPreferences(getString(R.string.saved_user_related), Context.MODE_PRIVATE);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_up, R.anim.slide_to_down);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (sharedPref.getString(getString(R.string.saved_username), "").isEmpty()) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void profileEditClicked() {
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    public void accountViewClicked() {
        Intent intent = new Intent(this, AccountEditActivity.class);
        startActivityForResult(intent, (short) R.integer.account_settings_request_code);
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }
}
