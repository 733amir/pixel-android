package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import co.lujun.androidtagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    String TAG = "ProfileEditActivity";

    TagContainerLayout tags;
    EditText newTag;
    String username;
    SharedPreferences sharedPref;
    ANImageView image;
    ANImageView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        getSupportActionBar().setTitle(R.string.profile_edit_title);

        tags = findViewById(R.id.profile_edit_tags);
        newTag = findViewById(R.id.profile_edit_new_tag);
        header = findViewById(R.id.profile_header_container);
        image = findViewById(R.id.profile_image_container);
        image.setDefaultImageResId(R.mipmap.default_profile_image);
        sharedPref = this.getSharedPreferences(getString(R.string.saved_user_related), Context.MODE_PRIVATE);

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
                        Log.i(TAG, "Account information response: " + response.toString());
                        try {
                            if (response.get("status").equals("ok")) {
                                JSONObject profile = new JSONObject(response.get("profile").toString());
                                header.setImageUrl(profile.getString("header"));

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

    public void addTag(View view) {
        tags.addTag(newTag.getText().toString());
        newTag.setText(null);
    }

    public void update(View view) {
        // TODO send information to server.
        finish();
    }

    @Override
    public void finish() {
        if (Locale.getDefault().getDisplayLanguage().equals("English"))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        else
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
