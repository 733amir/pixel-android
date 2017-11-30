package com.rahnemacollege.pixel;

import android.media.Image;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadPost extends AppCompatActivity implements View.OnClickListener {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_post);
        imageView = findViewById(R.id.post_image_insert);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.6));

        android.view.ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.width = (int) (width * 0.8 * 0.45);
        layoutParams.height = (int) (width * 0.8 * 0.45);
        imageView.setLayoutParams(layoutParams);
        imageView.setOnClickListener(this);

        ImageView locationImage = findViewById(R.id.post_location_insert);
        android.view.ViewGroup.LayoutParams layoutParams_loc = locationImage.getLayoutParams();
        layoutParams_loc.width = (int) (width * 0.8 * 0.1);
        layoutParams_loc.height = (int) (width * 0.8 * 0.1);
        locationImage.setLayoutParams(layoutParams_loc);

        Button addPostBtn = findViewById(R.id.add_post_btn);
        addPostBtn.setText("add");


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.post_image_insert:
                selectImage();
                break;
            case R.id.post_location_insert:
                break;
        }
    }


    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPost.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
//                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
//                try {
//                    Bitmap bitmap;
//                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//
//                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
//                            bitmapOptions);
                Bitmap bitmap= (Bitmap)data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);

//                    String path = android.os.Environment
//                            .getExternalStorageDirectory()
//                            + File.separator
//                            + "Phoenix" + File.separator + "default";
//                    f.delete();
//                    OutputStream outFile = null;
//                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
//                    try {
//                        outFile = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
//                        outFile.flush();
//                        outFile.close();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path_from gallery", picturePath+"");
                imageView.setImageBitmap(thumbnail);
            }
        }
    }
}

//    private void selectImage() {
//
//        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPost.this);
//        builder.setTitle("Add Photo!");
//        builder.setItems(options, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo")) {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
////                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
////                    imageUri = Uri.fromFile(f);
////                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
////                    startActivityForResult(intent, 1);
////                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    // Ensure that there's a camera activity to handle the intent
////                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////                        // Create the File where the photo should go
////                        File photoFile = null;
////                        try {
////                            photoFile = createImageFile();
////                        } catch (IOException ex) {
////                            // Error occurred while creating the File...
////                        }
////                        // Continue only if the File was successfully created
////                        if (photoFile != null) {
////                            Uri photoURI = FileProvider.getUriForFile(UploadPost.this,
////                                    "com.example.android.fileprovider",
////                                    photoFile);
////                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
////                            startActivityForResult(takePictureIntent, 1);
////                        }
////                    }
//                } else if (options[item].equals("Choose from Gallery")) {
//                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(intent, 2);
//
//                } else if (options[item].equals("Cancel")) {
//                    dialog.dismiss();
//                }
//            }
//        });
//        builder.show();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 1) {
////                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
////                bmOptions.inJustDecodeBounds = true;
////                BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
////                Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
////                imageView.setImageBitmap(bitmap);
////                galleryAddPic();
//
//
////                File f = new File(Environment.getExternalStorageDirectory().toString());
////                for (File temp : f.listFiles()) {
////                    if (temp.getName().equals("temp.jpg")) {
////                        f = temp;
////                        break;
////                    }
////                }
////                try {
////                    Bitmap bitmap;
////                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
////
////                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
////                            bitmapOptions);
////
////                    imageView.setImageBitmap(bitmap);
////
////                    String path = android.os.Environment
////                            .getExternalStorageDirectory()
////                            + File.separator
////                            + "Phoenix" + File.separator + "default";
////                    f.delete();
////                    OutputStream outFile = null;
////                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
////                    try {
////                        outFile = new FileOutputStream(file);
////                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
////                        outFile.flush();
////                        outFile.close();
////                    } catch (FileNotFoundException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
//                Bitmap bitmap=(Bitmap)data.getExtras().get("data");
//                imageView.setImageBitmap(bitmap);
//
//
//            } else if (requestCode == 2) {
//
//                Uri selectedImage = data.getData();
//                String[] filePath = {MediaStore.Images.Media.DATA};
//                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
//                c.moveToFirst();
//                int columnIndex = c.getColumnIndex(filePath[0]);
//                String picturePath = c.getString(columnIndex);
//                c.close();
//                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
//                Log.e("path_image from gallery", picturePath + "");
//                imageView.setImageBitmap(thumbnail);
//            }
//        }
//    }
//
////    String mCurrentPhotoPath;
////
////    private File createImageFile() throws IOException {
////        // Create an image file name
////        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
////        String imageFileName = "JPEG_" + timeStamp + "_";
////        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////        File image = File.createTempFile(
////                imageFileName,  /* prefix */
////                ".jpg",         /* suffix */
////                storageDir      /* directory */
////        );
////
////        // Save a file: path for use with ACTION_VIEW intents
////        mCurrentPhotoPath = image.getAbsolutePath();
////        return image;
////    }
////
////    private void galleryAddPic() {
////        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////        File f = new File(mCurrentPhotoPath);
////        Uri contentUri = Uri.fromFile(f);
////        mediaScanIntent.setData(contentUri);
////        this.sendBroadcast(mediaScanIntent);
////    }
//
////    private void setPic() {
////        // Get the dimensions of the View
////        int targetW = imageView.getWidth();
////        int targetH = imageView.getHeight();
////
////        // Get the dimensions of the bitmap
////        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
////        bmOptions.inJustDecodeBounds = true;
////        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
////        int photoW = bmOptions.outWidth;
////        int photoH = bmOptions.outHeight;
////
////        // Determine how much to scale down the image
////        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
////
////        // Decode the image file into a Bitmap sized to fill the View
////        bmOptions.inJustDecodeBounds = false;
////        bmOptions.inSampleSize = scaleFactor;
////        bmOptions.inPurgeable = true;
////
////        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
////        imageView.setImageBitmap(bitmap);
////    }
//
//
////    @Override
////    public void onBackPressed() {
////        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)));
////        super.onBackPressed();
////    }
//
//
////    @Override
////    public void finish() {
////        setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f)));
////    }
//}
//

