package com.rahnemacollege.pixel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity {

    ExploreFragment exploreFragment;
    SearchFragment searchFragment;
    UploadFragment uploadFragment;
    NotificationFragment notificationFragment;
    ProfileFragment profileFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean status = false;
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.home_nav_explore:
                    transaction.replace(R.id.home_container, exploreFragment);
                    status = true;
                    break;
                case R.id.home_nav_search:
                    transaction.replace(R.id.home_container, searchFragment);
                    status = true;
                    break;
                case R.id.home_nav_me:
                    transaction.replace(R.id.home_container, profileFragment);
                    status = true;
                    break;
            }
            transaction.addToBackStack(null);
            transaction.commit();
            return status;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        exploreFragment = new ExploreFragment();
        searchFragment = new SearchFragment();
        uploadFragment = new UploadFragment();
        notificationFragment = new NotificationFragment();
        profileFragment = new ProfileFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.home_container, exploreFragment).commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.home_nav_me);
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
