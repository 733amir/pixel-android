package com.rahnemacollege.pixel;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FirstPageActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentClickHandler,
        SignUpFragment.SignUpFragmentClickHandler {

    TabLayout tabLayout;
    ViewPager viewPager;
    MyFragmentPagerAdapter firstPageFragmentAdapter;
    public RequestQueue netQ;

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

        // Config tabLayout of FirstPage
        tabLayout = findViewById(R.id.first_page_tab);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).select();
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
    public void loginClicked(String username, String password) {
        findViewById(R.id.first_page_loading).setVisibility(View.VISIBLE);

        if (true) { // TODO Connect to server and check username and password.
            findViewById(R.id.first_page_loading).setVisibility(View.INVISIBLE);

            Intent intent = new Intent(this, ExploreActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_from_down, R.anim.slide_to_up);
        }
    }

    /**
     * When SignUp button in SignUpFragment is clicked this method gets called and register user on
     * the server.
     *
     * @param fullname Fullname user inputed.
     * @param username Username user inputed.
     * @param password Password user inputed.
     * @param email Email user inputed.
     */
    public void signUpClicked(String fullname, String username, String password, String email) {
        findViewById(R.id.first_page_loading).setVisibility(View.VISIBLE);
        setEnabledAll((ViewGroup) findViewById(R.id.signup_layout), false);
        /*
        RequestQueue queue = Volley.newRequestQueue(this);
        Map<String, String> postParam = new HashMap<>();
        String url = "192.168.10.139/users/signup/";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(postParam), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        queue.add(jsonObjectRequest);


        tabLayout.getTabAt(0).select();
        ((LoginFragment) firstPageFragmentAdapter.getItem(0)).fillUsernamePassword(username, password);
        */
    }

    private void setEnabledAll(ViewGroup vg, boolean enable){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (child instanceof ViewGroup){
                setEnabledAll((ViewGroup)child, enable);
            }
        }
    }
}