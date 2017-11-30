package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class AccountEditActivity extends AppCompatActivity {

    String TAG = "AccountEditActivity";

    String username;
    EditText username_field, email_field, old_password, new_password;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_edit);

        getSupportActionBar().setTitle(R.string.account_title);

        username_field = findViewById(R.id.account_username);
        email_field = findViewById(R.id.account_email);
        old_password = findViewById(R.id.account_password_current);
        new_password = findViewById(R.id.account_password_new);

        sharedPref = this.getSharedPreferences(getString(R.string.saved_user_related), Context.MODE_PRIVATE);

        username = sharedPref.getString(getString(R.string.saved_username), "");
        if (username.isEmpty()) {
            Log.e(TAG, "NO USERNAME PRESENTED!");
            finish();
        }

        AndroidNetworking.initialize(this);

        // Get user account settings.
        AndroidNetworking.get(getString(R.string.api_account))
                .addQueryParameter("username", username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Account information response: " + response.toString());
                        try {
                            if (response.get("status").equals("ok")) {
                                JSONObject info = new JSONObject(response.get("account").toString());
                                username_field.setText((String)info.get("username"));
                                email_field.setText((String)info.get("email"));
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

    public void update(View view) {
        if (old_password.getText().toString().isEmpty()) {
            old_password.setError(getString(R.string.account_is_empty));
        }

        if (email_field.getText().toString().isEmpty()) {
            email_field.setError(getString(R.string.account_is_empty));
        } else if (SignUpFragment.isValidEmail(email_field.getText().toString())) {
            AndroidNetworking.post(getString(R.string.api_account))
                    .addBodyParameter("username", username_field.getText().toString())
                    .addBodyParameter("email", email_field.getText().toString())
                    .addBodyParameter("old_password", old_password.getText().toString())
                    .addBodyParameter("new_password", (!new_password.getText().toString().isEmpty() ? new_password : old_password).getText().toString())
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.i(TAG, "Account update response: " + response.toString());
                            try {
                                if (response.get("status").equals("ok")) {
                                    finish();
                                } else if (response.get("desc").equals("Username or Password was wrong!")) {
                                    old_password.setError(getString(R.string.account_wrong_password));
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
        } else {
            email_field.setError(getString(R.string.account_email_not_valid));
        }
    }

    public void logout(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_username), null);
        editor.putString(getString(R.string.saved_token), null);
        editor.putBoolean(getString(R.string.saved_login_status), false);
        editor.apply();

        finish();
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
