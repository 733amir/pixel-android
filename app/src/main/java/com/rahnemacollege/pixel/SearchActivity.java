package com.rahnemacollege.pixel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    String TAG = "Search Activity";

    String access_token, username;
    EditText searchEditText;
    Button searchButton;
    SharedPreferences sharedPref;

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    //TODO change the quantity of items
    private static final int NUM_FRIENDS_ITEM = 10;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        getSupportActionBar().setTitle(R.string.search_title);

        searchEditText = findViewById(R.id.search_et);
        searchButton = findViewById(R.id.search_b);

        friendsRecyclerView = findViewById(R.id.search_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setHasFixedSize(true);

        friendsAdapter = new FriendsAdapter(NUM_FRIENDS_ITEM);
        friendsRecyclerView.setAdapter(friendsAdapter);


        sharedPref = this.getSharedPreferences(Constants.USER_INFO, Context.MODE_PRIVATE);

        access_token = sharedPref.getString(Constants.ACCESS_TOKEN, "");
        username = sharedPref.getString(Constants.USERNAME, "");
        if (access_token.isEmpty()) {
            Log.e(TAG, "NO TOKEN PRESENTED!");
            finish();
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                friendsAdapter.removeFriendsArrayList();
                String searchInput = searchEditText.getText().toString();

                AndroidNetworking.post(getString(R.string.api_friend_request))
                        .addHeaders(Constants.AUTHORIZATION, access_token)
                        .addPathParameter("user1", username)
                        .addPathParameter("user2", searchInput)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i(TAG, "Search get information response: " + response.toString());

                                String status = null;

                                try {
                                    status = response.getString(Constants.STATUS);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                if (status == null) {
                                    Log.e(TAG, "Search get information response have no status parameter.");
                                } else if (status.equals(Constants.OK)) {
                                    Log.e(TAG, "is now friend!!! :D");
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });
//
//                AndroidNetworking.get(getString(R.string.api_search))
//                        .addHeaders(Constants.AUTHORIZATION, access_token)
//                        .addPathParameter(Constants.SEARCH, searchInput)
//                        .build()
//                        .getAsJSONObject(new JSONObjectRequestListener() {
//                            @Override
//                            public void onResponse(JSONObject response) {
//                                Log.i(TAG, "Search get information response: " + response.toString());
//
//                                String status = null;
//
//                                try {
//                                    status = response.getString(Constants.STATUS);
//                                } catch (JSONException e) {
//                                    Log.e(TAG, "Search get information response JSONException: " + e.toString());
//                                }
//                                if (status == null) {
//                                    Log.e(TAG, "Search get information response have no status parameter.");
//                                } else if (status.equals(Constants.OK)) {
//                                    try {
//                                        JSONArray array = response.getJSONArray(Constants.OBJECT);
//                                        for (int i = 0; i < array.length(); i++) {
//                                            String username2 = array.getString(i);
//                                            Log.e(TAG, username2);
//                                            AndroidNetworking.get(getString(R.string.api_profile))
//                                                    .addPathParameter(Constants.USERNAME, username2)
//                                                    .build()
//                                                    .getAsJSONObject(new JSONObjectRequestListener() {
//                                                        @Override
//                                                        public void onResponse(JSONObject response) {
//                                                            Log.i(TAG, "Search profile get info response: " + response.toString());
//
//                                                            String status = null;
//
//                                                            try {
//                                                                status = response.getString(Constants.STATUS);
//                                                            } catch (JSONException e) {
//                                                                Log.e(TAG, "Search profile get info response JSONException: " + e.toString());
//                                                            }
//
//                                                            if (status == null) {
//                                                                Log.e(TAG, "Search profile get info response have no status parameter.");
//                                                            } else if (status.equals(Constants.OK)) {
//                                                                try {
//                                                                    JSONObject object = response.getJSONObject(Constants.OBJECT);
//                                                                    friendsAdapter.addFriendForAdapter(object.getString(Constants.FULLNAME), object.getString(Constants.PROFILE_PHOTO));
//                                                                } catch (JSONException e) {
//                                                                    Log.e(TAG, "Search profile get info response was incomplete.");
//                                                                    e.printStackTrace();
//                                                                }
//                                                            }
//
//                                                        }
//
//                                                        @Override
//                                                        public void onError(ANError anError) {
//
//                                                        }
//                                                    });
//                                        }
//                                    } catch (JSONException e) {
//                                        Log.e(TAG, "Search get info response was incomplete.");
//                                        e.printStackTrace();
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onError(ANError anError) {
//
//                            }
//                        });
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