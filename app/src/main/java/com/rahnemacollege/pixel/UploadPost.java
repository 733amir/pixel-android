package com.rahnemacollege.pixel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class UploadPost extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        ImageView imageView = findViewById(R.id.post_image_insert);
        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = (int) (width * 0.8 * 0.45);
        layoutParams.height = (int) (width * 0.8 * 0.45);
        imageView.setLayoutParams(layoutParams);

        ImageView locationImage=findViewById(R.id.post_location_insert);
        android.view.ViewGroup.LayoutParams layoutParams_loc = locationImage.getLayoutParams();
        layoutParams_loc.width = (int) (width * 0.8 * 0.1);
        layoutParams_loc.height = (int) (width * 0.8 * 0.1);
        locationImage.setLayoutParams(layoutParams_loc);

        Button addPostBtn=findViewById(R.id.add_post_btn);
        addPostBtn.setText("add");


    }
}
