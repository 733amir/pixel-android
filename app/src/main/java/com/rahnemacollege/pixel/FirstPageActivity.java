package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONException;
import org.json.JSONObject;


public class FirstPageActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentClickHandler,
        SignUpFragment.SignUpFragmentClickHandler {

    final String TAG = "FirstPageActivity";

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter firstPageFragmentAdapter;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        AndroidNetworking.initialize(getApplicationContext());

        // Customize actionBar layout
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.first_page_title);

        // Config fragmentPagerAdapter of FirstPage
        firstPageFragmentAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        firstPageFragmentAdapter.addFragment(new LoginFragment(), getString(R.string.firstpage_b_login));
        firstPageFragmentAdapter.addFragment(new SignUpFragment(), getString(R.string.firstpage_b_signup));

        // Config viewPager of FirstPage
        viewPager = findViewById(R.id.first_page_container);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(firstPageFragmentAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                hideKeyboard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        // Config tabLayout of FirstPage
        tabLayout = findViewById(R.id.first_page_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();

        // Shared preferences to share access_token, token and login status.
        sharedPref = this.getSharedPreferences(getString(R.string.user_info), Context.MODE_PRIVATE);
        if (!sharedPref.getString(Constants.ACCESS_TOKEN, "").isEmpty()) {
            gotoExploreActivity();
            finish();
        }
    }

    public void forgetPasswordClicked() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
        // TODO add animate here and in ResetPasswordActivity finish function.
    }

    public void loginClicked(final String username, String password) {
        hideKeyboard();
        findViewById(R.id.first_page_loading).setVisibility(View.VISIBLE);
        setEnabledAll((ViewGroup) findViewById(R.id.login_layout), false);

        AndroidNetworking.post(getString(R.string.api_login))
                .addHeaders("Content-Type", "application/x-www-form-urlencoded")
                .addBodyParameter("client_id", "trusted-app")
                .addBodyParameter("client_secret", "secret")
                .addBodyParameter("grant_type", "password")
                .addBodyParameter("username", username)
                .addBodyParameter("password", password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Login response: " + response.toString());

                        String access_token = null;

                        try {
                            access_token = response.getString(Constants.ACCESS_TOKEN);
                        } catch (JSONException e) {
                            Log.e(TAG, "Login response JSONException: " + e.toString());
                        }

                        if (access_token == null) {
                            Log.e(TAG, "Login response have no access_token parameter.");
                        } else {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(Constants.ACCESS_TOKEN, "Bearer " + access_token);
                            editor.putString(Constants.USERNAME, username);
                            editor.apply();

                            finish();
                            gotoExploreActivity();
                        }

                        findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                        setEnabledAll((ViewGroup) findViewById(R.id.login_layout), true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Login request error: " + anError.toString());

                        if (anError.getErrorCode() == 400) {
                            showMessage(getString(R.string.login_wrong_username_password));
                        }

                        findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                        setEnabledAll((ViewGroup) findViewById(R.id.login_layout), true);
                    }
                });
    }

    public void showMessage(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_LONG).show();
    }

    public void gotoExploreActivity() {
        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_down, R.anim.slide_to_up);
    }

    public void signUpClicked(final String fullname, final String username, final String password, final String email) {
        hideKeyboard();
        findViewById(R.id.first_page_loading).setVisibility(View.VISIBLE);
        setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), false);

        JSONObject body = new JSONObject();
        try {
            body.put("name", fullname);
            body.put("username", username);
            body.put("password", password);
            body.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(getString(R.string.api_register))
                .addJSONObjectBody(body)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Signup response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Signup response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Signup response have no status parameter.");
                        } else if (status.equals(Constants.USERNAME_EXISTS)) {
                            ((EditText) findViewById(R.id.signup_et_username)).setError(getString(R.string.signup_username_exists));
                        } else if (status.equals((Constants.EMAIL_EXISTS))) {
                            ((EditText) findViewById(R.id.signup_et_email)).setError(getString(R.string.signup_email_exists));
                        } else if (status.equals(Constants.OK)) {
                            signedUp(username, password);
                        }

                        findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                        setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), true);
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Login request error: " + anError.toString());

                        findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                        setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), true);
                    }
                });
    }

    public void signedUp(String username, String password) {
        ((SignUpFragment) firstPageFragmentAdapter.getItem(1)).resetAll();
        tabLayout.getTabAt(0).select();
        ((LoginFragment) firstPageFragmentAdapter.getItem(0)).fillUsernamePassword(username, password);
    }

    private void setEnabledAll(ViewGroup vg, boolean enable) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup) {
                setEnabledAll((ViewGroup) child, enable);
            }
        }
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}