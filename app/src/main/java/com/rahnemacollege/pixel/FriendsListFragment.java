package com.rahnemacollege.pixel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FriendsListFragment extends Fragment {
    String TAG = "Friends Fragment";

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    //TODO change the quantity of items
    private static final int NUM_FRIENDS_ITEM = 10;

    public String username;

    public FriendsListFragment() {
        // Required empty public constructor
    }

    public void setArgs(String username) {
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        friendsRecyclerView = view.findViewById(R.id.friends_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setHasFixedSize(true);

        friendsAdapter = new FriendsAdapter(NUM_FRIENDS_ITEM);
        friendsRecyclerView.setAdapter(friendsAdapter);

        AndroidNetworking.get(getString(R.string.api_friends))
                .addQueryParameter("username", username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Friends get information response: " + response.toString());

                        try {
                            if (response.get("status").equals("ok")) {
                                JSONArray friends = new JSONArray(response.getString("friends"));
                                for (int i = 0; i < friends.length(); i++) {
                                    String friend = friends.getString(i);
                                    AndroidNetworking.get(getString(R.string.api_profile))
                                            .addQueryParameter("username", friend)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.i(TAG, "Friends profiles get information response: " + response.toString());
                                                    try {
                                                        if (response.get("status").equals("ok")) {
                                                            JSONObject friendProfile = new JSONObject(response.get("profile").toString());
                                                            friendsAdapter.addFriendForAdapter(response.getString("fullname"), friendProfile.getString("image"));
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onError(ANError anError) {

                                                }
                                            });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        return view;
    }
}