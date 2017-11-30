package com.rahnemacollege.pixel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class testCamera extends AppCompatActivity implements View.OnClickListener {
ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_camera);
        imageView=findViewById(R.id.testCameraImageView);
        Button button=findViewById(R.id.testCameraBtn);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    if(resultCode==RESULT_OK && requestCode==1){
        Bundle bundle=data.getExtras();
        Bitmap bitmap=(Bitmap)bundle.get("data");
        imageView.setImageBitmap(bitmap);
    }
    }
}
