package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class AccountEditActivity extends AppCompatActivity {

    String TAG = "AccountEditActivity";

    String access_token, username;
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

        sharedPref = this.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);

        access_token = sharedPref.getString(Constants.ACCESS_TOKEN, "");
        username = sharedPref.getString(Constants.USERNAME, "");
        if (access_token.isEmpty()) {
            Log.e(TAG, "NO TOKEN PRESENTED!");
            finish();
        }

        // Get user account settings.
        AndroidNetworking.get(getString(R.string.api_account))
                .addPathParameter(Constants.USERNAME, username)
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Account settings response: " + response.toString());
                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Account settings response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Account settings response have no status parameter.");
                        } else if (status.equals(Constants.NOT_FOUND)) {
                            showMessage(getString(R.string.account_wrong_username));
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONObject object = response.getJSONObject(Constants.OBJECT);
                                String username = object.getString(Constants.USERNAME);
                                String email = object.getString(Constants.EMAIL);

                                username_field.setText(username);
                                email_field.setText(email);
                            } catch (JSONException e) {
                                Log.e(TAG, "Account settings response no username or email parameters.");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "Account settings response error: " + error.toString());
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
            // TODO change update.

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
        sharedPref.edit().putString(Constants.ACCESS_TOKEN, "").apply();
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

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
