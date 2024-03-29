package com.rahnemacollege.pixel;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class FriendsListFragment extends Fragment {
    String TAG = "Friends Fragment";

    String username, access_token;

    ImageView search;
    FriendsClickHandler clickHandler;

    private RecyclerView friendsRecyclerView;
    private FriendsAdapter friendsAdapter;
    //TODO change the quantity of items
    private static final int NUM_FRIENDS_ITEM = 10;

    public interface FriendsClickHandler {
        void searchClicked();
    }

    public FriendsListFragment() {
        // Required empty public constructor
    }

    public void setArgs(String access_token, String username) {
        this.username = username;
        this.access_token = access_token;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends_list, container, false);
        search = view.findViewById(R.id.friends_search);
        clickHandler = (FriendsListFragment.FriendsClickHandler) getActivity();
        friendsRecyclerView = view.findViewById(R.id.friends_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        friendsRecyclerView.setLayoutManager(layoutManager);
        friendsRecyclerView.setHasFixedSize(true);

        friendsAdapter = new FriendsAdapter(NUM_FRIENDS_ITEM);
        friendsRecyclerView.setAdapter(friendsAdapter);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.searchClicked();
            }
        });

        AndroidNetworking.get(getString(R.string.api_friends))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addPathParameter(Constants.USERNAME, username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Friends get information response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Friends get information response JSONException: " + e.toString());
                        }
                        if (status == null) {
                            Log.e(TAG, "Friends get information response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONArray array = response.getJSONArray(Constants.OBJECT);
                                for (int i = 0; i < array.length(); i++) {
                                    String username2 = array.getString(i);
                                    Log.e(TAG, username2);
                                    AndroidNetworking.get(getString(R.string.api_profile))
                                            .addPathParameter(Constants.USERNAME, username2)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    Log.i(TAG, "Friend profile get info response: " + response.toString());

                                                    String status = null;

                                                    try {
                                                        status = response.getString(Constants.STATUS);
                                                    } catch (JSONException e) {
                                                        Log.e(TAG, "Friend profile get info response JSONException: " + e.toString());
                                                    }

                                                    if (status == null) {
                                                        Log.e(TAG, "Friend profile get info response have no status parameter.");
                                                    } else if (status.equals(Constants.OK)) {
                                                        try {
                                                            JSONObject object = response.getJSONObject(Constants.OBJECT);
                                                            friendsAdapter.addFriendForAdapter(object.getString(Constants.FULLNAME), object.getString(Constants.PROFILE_PHOTO));
                                                        } catch (JSONException e) {
                                                            Log.e(TAG, "Friend profile get info response was incomplete.");
                                                            e.printStackTrace();
                                                        }
                                                    }

                                                }

                                                @Override
                                                public void onError(ANError anError) {

                                                }
                                            });
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Friends get info response was incomplete.");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
        return view;
    }
}