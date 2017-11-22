package com.rahnemacollege.pixel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class ExploreActivity extends AppCompatActivity {

    HomeFragment homeFragment;
    FriendsListFragment friendsListFragment;
    ProfileFragment profileFragment;
    MyFragmentPagerAdapter explorePageFragmentAdapter;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean status = false;
            switch (item.getItemId()) {
                case R.id.home_nav_explore:
                    viewPager.setCurrentItem(0);
                    status = true;
                    break;
                case R.id.home_nav_friends_list:
                    viewPager.setCurrentItem(1);
                    status = true;
                    break;
                case R.id.home_nav_me:
                    viewPager.setCurrentItem(2);
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

        // Create and config explore fragmentPagerAdapter
        explorePageFragmentAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        explorePageFragmentAdapter.addFragment(homeFragment, getString(R.string.explore_nav_home));
        explorePageFragmentAdapter.addFragment(friendsListFragment, getString(R.string.explore_nav_friends_list));
        explorePageFragmentAdapter.addFragment(profileFragment, getString(R.string.explore_nav_me));

        // Config viewPager of explore activity
        viewPager = findViewById(R.id.explore_container);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(explorePageFragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
}
