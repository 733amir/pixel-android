package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileEditActivity extends AppCompatActivity {

    String TAG = "ProfileEditActivity";

    TagContainerLayout tags;

    EditText newTag, bio, fullname;
    String username, access_token, headerImage, profileImage;
    SharedPreferences sharedPref;
    CircleImageView image;
    ImageView header;
    AppCompatActivity current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setTitle(R.string.profile_edit_title);

        tags = findViewById(R.id.profile_edit_tags);
        newTag = findViewById(R.id.profile_edit_new_tag);
        header = findViewById(R.id.profile_header_container);
        image = findViewById(R.id.profile_image_container);
        sharedPref = this.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);
        current = this;
        bio = findViewById(R.id.profile_bio);
        fullname = findViewById(R.id.profile_fullname);

        access_token = sharedPref.getString(Constants.ACCESS_TOKEN, "");
        username = sharedPref.getString(Constants.USERNAME, "");
        if (access_token.isEmpty()) {
            Log.e(TAG, "NO ACCESS_TOKEN PRESENTED!");
            finish();
        }

        AndroidNetworking.get(getString(R.string.api_user))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addPathParameter(Constants.USERNAME, username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile get info response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Profile get info response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Profile get info response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONObject object = response.getJSONObject(Constants.OBJECT);

                                fullname.setText(object.getString(Constants.FULLNAME));
                                bio.setText(object.getString(Constants.BIO));

                                headerImage = object.getString(Constants.COVER_PHOTO);
                                Glide.with(header)
                                        .load(Constants.addAuthorization(getString(R.string.api_profile_photo) + headerImage, access_token))
                                        .into(header);

                                profileImage = object.getString(Constants.PROFILE_PHOTO);
                                Glide.with(image)
                                        .load(Constants.addAuthorization(getString(R.string.api_profile_photo) + profileImage, access_token))
                                        .into(image);
                            } catch (JSONException e) {
                                Log.e(TAG, "Profile get response was incomplete.");
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile get info request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });

        AndroidNetworking.get(getString(R.string.api_interests))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile get all interests response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Profile get all interests response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Profile get info response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONArray all = response.getJSONArray(Constants.INTERESTS);
                                for (int i = 0; i < all.length(); i++) {
                                    tags.addTag(all.getJSONObject(i).getString(Constants.INTEREST));
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Profile get all interests response was incomplete.");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile get info request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });


        tags.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                tags.removeTag(position);
            }

            @Override
            public void onTagLongClick(int position, String text) {
                newTag.setText(tags.getTagText(position));
                tags.removeTag(position);
            }

            @Override
            public void onTagCrossClick(int position) {
                tags.removeTag(position);
            }
        });
    }

    public void addTag(View view) {
        if (!newTag.getText().toString().isEmpty()) {
            tags.addTag(newTag.getText().toString());
            newTag.setText(null);
        }
    }

    public void update(View view) {
        // TODO upload new profile header and image.
        // TODO integrate upload with server.

        JSONObject body = new JSONObject();
        try {
            body.put(Constants.BIO, bio.getText().toString());
            body.put(Constants.NAME, fullname.getText().toString());
            body.put("profilephoto", "0.jpg");
            body.put("coverphoto", "1.jpg");
        } catch (JSONException e) {
            Log.e(TAG, "Parsing name and bio.");
            e.printStackTrace();
        }

        AndroidNetworking.put(getString(R.string.api_profile))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addPathParameter(Constants.USERNAME, username)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile update fullname, bio, cover and profile photo response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Profile update fullname, bio, cover and profile photo response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Profile update fullname, bio, cover and profile photo response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, username + " Profile update fullname, bio, cover and profile photo request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });

        body = new JSONObject();
        JSONArray new_tags = new JSONArray();
        JSONObject new_tag;
        try {
            for (int i = 0; i < tags.getTags().size(); i++) {
                new_tag = new JSONObject();
                new_tag.put(Constants.INTEREST, tags.getTagText(i));
                new_tags.put(new_tag);
            }
            body.put(Constants.INTERESTS, new_tags);
        } catch (JSONException e) {
            Log.e(TAG, "Parsing tags problem.");
            e.printStackTrace();
        }
        AndroidNetworking.put(getString(R.string.api_interests))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile update interests response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Profile update interests response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Profile update interests response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            finish();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile update interests request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });
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
