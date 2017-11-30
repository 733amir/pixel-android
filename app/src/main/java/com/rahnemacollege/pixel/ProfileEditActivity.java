package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    String TAG = "ProfileEditActivity";

    TagContainerLayout tags;
    EditText newTag, bio, fullname;
    String username;
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
        sharedPref = this.getSharedPreferences(getString(R.string.saved_user_related), Context.MODE_PRIVATE);
        current = this;
        bio = findViewById(R.id.profile_bio);
        fullname = findViewById(R.id.profile_fullname);

        username = sharedPref.getString(getString(R.string.saved_username), "");
        if (username.isEmpty()) {
            Log.e(TAG, "NO USERNAME PRESENTED!");
            finish();
        }

        AndroidNetworking.get(getString(R.string.api_profile))
                .addQueryParameter("username", username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile get information response: " + response.toString());
                        try {
                            if (response.get("status").equals("ok")) {
                                JSONObject profile = new JSONObject(response.get("profile").toString());

                                Glide.with(current).load(profile.getString("header")).into(header);
                                Glide.with(current).load(profile.getString("image")).into(image);

                                JSONArray interests = profile.getJSONArray("interest");
                                for (int i = 0; i < interests.length(); i++) {
                                    tags.addTag(interests.getString(i));
                                }

                                bio.setText(profile.getString("bio"));

                                fullname.setText(response.getString("fullname"));
                            } else if (response.has("desc")) {
                                Log.e(TAG, response.get("desc").toString());
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, error.toString());
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

        JSONArray newTags = new JSONArray(tags.getTags());
        AndroidNetworking.post(getString(R.string.api_profile))
                .addQueryParameter("username", username)
                .addBodyParameter("fullname", fullname.getText().toString())
                .addBodyParameter("bio", bio.getText().toString())
                .addBodyParameter("interests", newTags.toString())
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile push information response: " + response.toString());
                        try {
                            if (response.get("status").equals("ok")) {
                                finish();
                            } else if (response.has("desc")) {
                                Log.e(TAG, response.get("desc").toString());
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, error.toString());
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        if (Locale.getDefault().getDisplayLanguage().equals("English"))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        else
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
