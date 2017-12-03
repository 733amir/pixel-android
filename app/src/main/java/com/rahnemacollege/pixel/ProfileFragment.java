package com.rahnemacollege.pixel;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.rahnemacollege.pixel.Utilities.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    String TAG = "ProfileFragment";

    View view;
    ImageView profile_edit, notification, account_view, header;
    CircleImageView image;
    ProfileClickHandler clickHandler;
    String username, access_token, profileImageUrl;

    RecyclerView tags;
    LinearLayoutManager tagsContainer;
    InterestsAdapter tagsAdapter;

    RecyclerView posts;
    LinearLayoutManager postsContainer;
    PostAdapter postsAdapter;

    TextView bio, fullname;

    public interface ProfileClickHandler {
        public void profileEditClicked();

        public void accountViewClicked();
    }

    public ProfileFragment() {
    }

    public void setArgs(String access_token, String username) {
        this.access_token = access_token;
        this.username = username;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_edit = view.findViewById(R.id.profile_edit);
        notification = view.findViewById(R.id.profile_notification);
        account_view = view.findViewById(R.id.profile_account);
        clickHandler = (ProfileClickHandler) getActivity();
        header = view.findViewById(R.id.profile_header_container);
        image = view.findViewById(R.id.profile_image_container);
        tags = view.findViewById(R.id.profile_interests);
        tagsContainer = new LinearLayoutManager(getActivity());
        tagsAdapter = new InterestsAdapter(10);
        posts = view.findViewById(R.id.profile_posts);
        postsContainer = new LinearLayoutManager(getActivity());
        postsAdapter = new PostAdapter(50);
        bio = view.findViewById(R.id.profile_bio);
        fullname = view.findViewById(R.id.profile_fullname);

        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.profileEditClicked();
            }
        });

        account_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickHandler.accountViewClicked();
            }
        });

        tagsContainer.setOrientation(LinearLayoutManager.HORIZONTAL);

        tags.setHasFixedSize(true);
        tags.setLayoutManager(tagsContainer);
        tags.setAdapter(tagsAdapter);

        posts.setHasFixedSize(true);
        posts.setLayoutManager(postsContainer);
        posts.setAdapter(postsAdapter);

        AndroidNetworking.get(getString(R.string.api_user))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addPathParameter(Constants.USERNAME, username)
                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.i(TAG, "Profile get information response: " + response.toString());
//                        try {
//                            if (response.get("status").equals("ok")) {
//                                JSONObject profile = new JSONObject(response.get("profile").toString());
//
//                                Glide.with(getActivity()).load(profile.getString("header")).into(header);
//                                Glide.with(getActivity()).load(profile.getString("image")).into(image);
//                                profileImageUrl = profile.getString("image");
//
//                                JSONArray interests = profile.getJSONArray("interest");
//                                for (int i = 0; i < interests.length(); i++) {
//                                    tagsAdapter.addTag(interests.getString(i));
//                                }
//
//                                bio.setText(profile.getString("bio"));
//
//                                fullname.setText(response.getString("fullname"));
//                            } else if (response.has("desc")) {
//                                Log.e(TAG, response.get("desc").toString());
//                            }
//                        } catch (JSONException e) {
//                            Log.e(TAG, e.toString());
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        Log.e(TAG, error.toString());
//                    }
//                });
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile get info response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Profile get info response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Profile get info response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONObject object = response.getJSONObject(Constants.OBJECT);

                                fullname.setText(object.getString(Constants.FULLNAME));
                                bio.setText(object.getString(Constants.BIO));

                                // TODO get interests.
                                //                                JSONArray interests = profile.getJSONArray("interest");
                                //                                for (int i = 0; i < interests.length(); i++) {
                                //                                    tags.addTag(interests.getString(i));
                                //                                }

                                // TODO load images
                                //                                Glide.with(current).load(profile.getString("header")).into(header);
                                //                                Glide.with(current).load(profile.getString("image")).into(image);
                            } catch (JSONException e) {
                                Log.e(TAG, "Profile get response was incomplete.");
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile get info request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });

        AndroidNetworking.get(getString(R.string.api_posts))
                .addQueryParameter("username", username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Posts get information response: " + response.toString());
                        try {
                            if (response.get("status").equals("ok")) {
                                JSONArray posts = new JSONArray(response.getString("posts"));
                                JSONObject post;
//                                String fullname, postImageUrl, profileImageUrl, location, caption, time, like_count, comment_count;
                                for (int i = 0; i < posts.length(); i++) {
                                    post = posts.getJSONObject(i);
                                    postsAdapter.addPost(
                                            fullname.getText().toString(),
                                            post.getString("time"),
                                            post.getString("location"),
                                            post.getString("like_count"),
                                            post.getString("comment_count"),
                                            post.getString("caption"),
                                            post.getString("image"),
                                            profileImageUrl
                                    );
                                    Log.i(TAG, "Posts size: " + posts.length());
                                }
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

        return view;
    }
}
