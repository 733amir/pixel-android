package com.rahnemacollege.pixel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FirstPageActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentClickHandler,
        SignUpFragment.SignUpFragmentClickHandler {

    final String TAG = "FirstPageActivity";

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter firstPageFragmentAdapter;
    public RequestQueue netQ;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        netQ = Volley.newRequestQueue(this);

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
                // Check if no view has focus:
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

        // Shared preferences to share username, token and login status.
        sharedPref = this.getSharedPreferences(getString(R.string.saved_user_related), Context.MODE_PRIVATE);
        // TODO check for username, token and login status. If user is logged in start next activity and finish this one.
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.saved_username), "");
        editor.putBoolean(getString(R.string.saved_login_status), false);
        editor.putString(getString(R.string.saved_token), "");
        editor.apply();
    }

    /**
     * When ForgetPassword TextView on LoginFragment is selected this method is called.
     */
    public void forgetPasswordClicked() {
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * When Login button is selected this method connect to server and verify the information and
     * then switch to user explore activity.
     *
     * @param username Username that user wrote.
     * @param password Password that user wrote.
     */
    public void loginClicked(final String username, String password) {
        findViewById(R.id.first_page_loading).setVisibility(View.VISIBLE);
        setEnabledAll((ViewGroup) findViewById(R.id.login_layout), false);

        String url = getString(R.string.api_login) + "?username=" + username + "&password=" + password;
        StringRequest loginReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "Response of Login request: " + response);
                try {
                    JSONObject res = new JSONObject(response);
                    if (res.has("status") && res.get("status").equals("ok")) {
                        // Save username.
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.saved_username), username);
                        editor.putBoolean(getString(R.string.saved_login_status), true);
                        editor.apply();

                        loggedIn();
                    } else if (res.has("desc")) {
                        loggedIn(res.get("desc").toString());
                    } else {
                        Log.e(TAG, "JSON response wasn't complete.");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Login respond wasn't JSON.");
                } finally {
                    findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                    setEnabledAll((ViewGroup) findViewById(R.id.login_layout), true);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error on Login request: " + error.toString());

                findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                setEnabledAll((ViewGroup) findViewById(R.id.login_layout), true);
            }
        });

        netQ.add(loginReq);
    }

    public void loggedIn(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_LONG).show();
    }

    public void loggedIn() {
        Intent intent = new Intent(this, ExploreActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_from_down, R.anim.slide_to_up);
    }

    /**
     * When SignUp button in SignUpFragment is clicked this method gets called and register user on
     * the server.
     *
     * @param fullname Fullname user inputed.
     * @param username Username user inputed.
     * @param password Password user inputed.
     * @param email    Email user inputed.
     */
    public void signUpClicked(final String fullname, final String username, final String password, final String email) {
        findViewById(R.id.first_page_loading).setVisibility(View.VISIBLE);
        setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), false);

        String url = getString(R.string.api_register);
        Log.i(TAG, "Sign up request url: " + url);
        StringRequest registerReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response to Sign up request: " + response);

                try {
                    JSONObject res = new JSONObject(response);
                    if (res.has("status") && res.get("status").equals("ok")) {
                        signedUp(username, password);
                    } else if (res.has("desc")) {
                        signedUp(res.get("desc").toString());
                    } else {
                        Log.e(TAG, "JSON response wasn't complete.");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Sign up response wasn't JSON.");
                } finally {
                    findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                    setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), true);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error
                Log.d(TAG, "Error on Sign up request: " + error.toString());

                findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);
                setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), true);
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("username", username);
                params.put("password", password);
                params.put("fullname", fullname);
                params.put("email", email);

                return params;
            }
        };

        netQ.add(registerReq);
    }

    public void signedUp(String desc) {
        Toast.makeText(this, desc, Toast.LENGTH_LONG).show();
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

    public RequestQueue getNetQ() {
        return netQ;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

//    @Override
//    public void finish() {
//        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
//    }
}