package com.rahnemacollege.pixel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class UploadPost extends AppCompatActivity {

    String TAG = "UploadPost";

    CropImageView post_image;
    private static final int CAMERA_REQUEST = 1888, GALLERY_REQUEST = 1889;
    private static final int LOCATION_PERMISSION_REQUEST = 1900;
    private String[] locationPerimissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};

    private LocationManager locationManager;
    private boolean isGPSenabled;
    TextView location_textView;
    ImageView location_icon;
    View view;
    private boolean isNetWorkEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.9), (int) (height * 0.8));

        post_image = findViewById(R.id.new_post_image);
        location_textView = findViewById(R.id.new_post_location_text);
        location_icon = findViewById(R.id.new_post_location_icon);

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSenabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        location_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UploadPost.this.view = view;
                Log.e("location_icon", "clicked");
                checkPermission_Location();
            }
        });
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        String source = getIntent().getStringExtra("source");
        if (source.equals("camera")) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        } else if (source.equals("gallery")) {
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri pickedImage = data.getData();
            String[] filePath = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(pickedImage, filePath, null, null, null);
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap photo = BitmapFactory.decodeFile(imagePath, options);

            post_image.setImageBitmap(photo);

        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            post_image.setImageBitmap(photo);
        } else {
            finish();
        }
    }

    public void imageRotate(View view) {
        post_image.rotateImage(90);
    }

    public void checkPermission_Location() {
        Log.e("checkPermission funct", "inside");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, locationPerimissions, LOCATION_PERMISSION_REQUEST);
            return;
        }
        getLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.e("onRequestPermission", "checked");
        if (requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
    }

    public void getLocation() {
        if (!isNetWorkEnabled) {
            Intent intent = new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS);
            startActivity(intent);
        }
        if (!isGPSenabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        Log.e("inGetLocation", "inside");
        SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                showLocation(location);
            }
        });

    }

    public void showLocation(Location location) {
        Log.e("showLocation", "inside");
        if (location != null) {

            // We are going to get the address for the current position
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String city = addresses.get(0).getLocality();
                String knownName = addresses.get(0).getFeatureName();
                location_textView.setText(city + "," + knownName);
                location_icon.setImageResource(R.drawable.ic_location_on_blue_24dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            location_textView.setText("Null location");
        }
    }
}
