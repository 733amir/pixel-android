package com.rahnemacollege.pixel;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends FragmentActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home_nav_home:
                    mTextMessage.setText(R.string.home_nav_home);
                    return true;
                case R.id.home_nav_search:
                    mTextMessage.setText(R.string.home_nav_search);
                    return true;
                case R.id.home_nav_upload:
                    mTextMessage.setText(R.string.home_nav_notification);
                    return true;
                case R.id.home_nav_notification:
                    mTextMessage.setText(R.string.home_nav_notification);
                    return true;
                case R.id.home_nav_me:
                    mTextMessage.setText(R.string.home_nav_me);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_from_up, R.anim.slide_to_down);
    }
}
