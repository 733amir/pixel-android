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

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.location.DetectedActivity;

import com.rahnemacollege.pixel.Utilities.Constants;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class UploadPost extends AppCompatActivity {

    String TAG = "UploadPost";

    Location location;
    boolean location_status = false;

    CropImageView post_image;
    private static final int CAMERA_REQUEST = 1888, GALLERY_REQUEST = 1889;
    private static final int LOCATION_PERMISSION_REQUEST = 1900;
    private String[] locationPerimissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET};

    private LocationManager locationManager;
    private boolean isGPSenabled;
    TextView location_textView;
    ImageView location_icon;
    LinearLayout location_container;
    View view;
    private boolean isNetWorkEnabled;

    String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        location = null;

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
        location_container = findViewById(R.id.new_post_location_holder);
        location_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location_status = !location_status;

                if (location_status) {
                    if (location != null) {
                        showLocation(location);
                    }

                    UploadPost.this.view = view;
                    Log.e("location_icon", "clicked");
                    checkPermission_Location();
                } else {
                    location_textView.setText("");
                    location_icon.setImageResource(R.drawable.ic_location_on_black_24dp);
                }
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

        access_token = getIntent().getStringExtra(Constants.ACCESS_TOKEN);
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
            this.location = location;

            // We are going to get the address for the current position
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());

            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String city = addresses.get(0).getLocality();
                String knownName = addresses.get(0).getFeatureName();
                location_textView.setText(city + ", " + knownName);
                location_icon.setImageResource(R.drawable.ic_location_on_blue_24dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            location_textView.setText("Null location");
        }
    }

    public void upload_post(View view) {
        Bitmap image = post_image.getCroppedImage();
        String caption = ((EditText) findViewById(R.id.new_post_caption)).getText().toString();

        try {
            File outputDir = getCacheDir(); // context being the Activity pointer
            File outputFile = File.createTempFile("temp", ".png", outputDir);
            FileOutputStream fOut = new FileOutputStream(outputFile);
            image.compress(Bitmap.CompressFormat.PNG, 95, fOut);
            fOut.flush();
            fOut.close();

            AndroidNetworking.upload(getString(R.string.api_post_upload))
                    .addHeaders(Constants.AUTHORIZATION, access_token)
                    .addMultipartFile(Constants.PHOTO, outputFile)
                    .addMultipartParameter(Constants.TEXT, caption)
                    .addMultipartParameter(Constants.LAT, String.valueOf(location_status? location.getLatitude(): 0))
                    .addMultipartParameter(Constants.LON, String.valueOf(location_status? location.getLongitude(): 0))
                    .build()
                    .setUploadProgressListener(new UploadProgressListener() {
                        @Override
                        public void onProgress(long bytesUploaded, long totalBytes) {
                            // TODO add a uploader progress bar.
                        }
                    })
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "Upload post response: " + response.toString());

                            String status = null;

                            try {
                                status = response.getString(Constants.STATUS);
                            } catch (JSONException e) {
                                Log.e(TAG, "Upload post response JSONException: " + e.toString());
                            }

                            if (status == null) {
                                Log.e(TAG, "Upload post response have no status parameter.");
                            } else if (status.equals(Constants.OK)) {
                                finish();
                            }
                        }

                        @Override
                        public void onError(ANError e) {
                            Log.e(TAG, "Upload post request error: " + e.toString() + " code: " + e.getErrorCode());
                            e.printStackTrace();
                        }
                    });

        } catch (IOException e) {
            Log.e(TAG, "Error on saving bitmap of cropped image.");
            e.printStackTrace();
        }
    }
}
