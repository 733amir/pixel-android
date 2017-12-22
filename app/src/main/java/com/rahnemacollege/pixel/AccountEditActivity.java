package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;


public class AccountEditActivity extends AppCompatActivity {

    String TAG = "AccountEditActivity";

    String access_token, username;
    EditText username_field, email_field, old_password, new_password, new_password_repeat;
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
        new_password_repeat = findViewById(R.id.account_password_new_repeat);

        sharedPref = this.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);

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
                        error.printStackTrace();
                    }
                });
    }

    public void update(View view) {
        boolean npwstat = false, npwrstat = false, emailstat = false;

        if (!old_password.getText().toString().isEmpty()) {

            if (new_password.getText().toString().isEmpty()) {
                new_password.setError(getString(R.string.account_is_empty));
            } else if (new_password.getText().length() < 5) {
                new_password.setError(getString(R.string.signup_short_string));
            } else {
                new_password.setError(null);
                npwstat = true;
            }

            if (new_password_repeat.getText().toString().isEmpty()) {
                new_password_repeat.setError(getString(R.string.account_is_empty));
            } else if (!new_password_repeat.getText().toString().equals(new_password.getText().toString())) {
                new_password_repeat.setError(getString(R.string.signup_password_not_matched));
            } else {
                new_password_repeat.setError(null);
                npwrstat = true;
            }
        } else {
            npwstat = npwrstat = true;
        }

        if (email_field.getText().toString().isEmpty()) {
            email_field.setError(getString(R.string.account_is_empty));
        } else if (!SignUpFragment.isValidEmail(email_field.getText().toString())) {
            email_field.setError(getString(R.string.account_email_not_valid));
        } else {
            email_field.setError(null);
            emailstat = true;
        }

        // TODO change update.
        if (npwrstat && npwstat && emailstat) {
            JSONObject body = new JSONObject();
            try {
                body.put(Constants.USERNAME, username);
                body.put(Constants.EMAIL, email_field.getText().toString());
                if (!old_password.getText().toString().isEmpty()) {
                    body.put(Constants.OLD_PASSWORD, old_password.getText().toString());
                    body.put(Constants.NEW_PASSWORD, new_password.getText().toString());
                }
            } catch (JSONException e) {
                Log.e(TAG, "update json parsing problem.");
                e.printStackTrace();
            }

            AndroidNetworking.put(getString(R.string.api_account))
                    .addHeaders(Constants.AUTHORIZATION, access_token)
                    .addPathParameter(Constants.USERNAME, username)
                    .addJSONObjectBody(body)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i(TAG, "Account settings update response: " + response.toString());
                    String status = null;

                    try {
                        status = response.getString(Constants.STATUS);
                    } catch (JSONException e) {
                        Log.e(TAG, "Account settings update response JSONException: " + e.toString());
                    }

                    if (status == null) {
                        Log.e(TAG, "Account settings update response have no status parameter.");
                    } else if (status.equals(Constants.NOT_FOUND)) {
                        showMessage(getString(R.string.account_wrong_username));
                    } else if (status.equals(Constants.WRONG_PASSWORD)) {
                        old_password.setError(getString(R.string.account_wrong_password));
                    } else if (status.equals(Constants.OK)) {
                        finish();
                    }
                }

                @Override
                public void onError(ANError error) {
                    Log.e(TAG, "Account settings update response error: " + error.getErrorCode());
                    error.printStackTrace();
                }
            });
        }
    }

    public void logout(View view) {
        sharedPref.edit().putString(Constants.ACCESS_TOKEN, "").apply();
        finish();
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

    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
