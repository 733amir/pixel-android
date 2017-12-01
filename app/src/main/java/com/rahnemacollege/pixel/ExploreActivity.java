package com.rahnemacollege.pixel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.github.clans.fab.FloatingActionMenu;

import java.io.FileOutputStream;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ExploreActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    String TAG = "ExploreActivity";

    HomeFragment homeFragment;
    FriendsListFragment friendsListFragment;
    ProfileFragment profileFragment;

    MyFragmentPagerAdapter explorePageFragmentAdapter;
    ViewPager contentViewPager;
    BottomNavigationView bottomNavigationView;

    SharedPreferences sharedPref;

    FloatingActionMenu uploadMenu;

    private static final int CAMERA_PERMISSION_REQUEST = 1890, GALLERY_PERMISSION_REQUEST = 1891;
    private String[] galleryPermissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String[] cameraPermissions = {Manifest.permission.CAMERA};

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

        // Floating Menu and Buttons
        uploadMenu = findViewById(R.id.upload_menu);

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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                uploadMenu.close(true);
            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                uploadMenu.close(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
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
        super.onActivityResult(requestCode, resultCode, data);

        // User is logged out.
        if (sharedPref.getString(getString(R.string.saved_username), "").isEmpty()) {
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        uploadMenu.close(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        if (requestCode == GALLERY_PERMISSION_REQUEST) {
            Intent intent = new Intent(this, UploadPost.class);
            intent.putExtra("source", "gallery");
            startActivity(intent);
        } else if (requestCode == CAMERA_PERMISSION_REQUEST) {
            Intent intent = new Intent(this, UploadPost.class);
            intent.putExtra("source", "camera");
            startActivity(intent);
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {}

    public void getPhotoFromCamera(View view) {
        if (EasyPermissions.hasPermissions(this, cameraPermissions)) {
            Intent intent = new Intent(this, UploadPost.class);
            intent.putExtra("source", "camera");
            startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(this, "Access for Camera",
                    CAMERA_PERMISSION_REQUEST, cameraPermissions);
        }
    }

    public void getPhotoFromGallery(View view) {
        if (EasyPermissions.hasPermissions(this, galleryPermissions)) {
            Intent intent = new Intent(this, UploadPost.class);
            intent.putExtra("source", "gallery");
            startActivity(intent);
        } else {
            EasyPermissions.requestPermissions(this, "Access for Gallery",
                    GALLERY_PERMISSION_REQUEST, galleryPermissions);
        }
    }
}
