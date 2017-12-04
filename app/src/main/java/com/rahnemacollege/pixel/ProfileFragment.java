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
    Context current;

    View view;
    ImageView profile_edit, notification, account_view, header;
    CircleImageView image;
    ProfileClickHandler clickHandler;
    String username, access_token, profileImage, headerImage;

    RecyclerView tags;
    LinearLayoutManager tagsContainer;
    InterestsAdapter tagsAdapter;

    RecyclerView posts;
    LinearLayoutManager postsContainer;
    PostAdapter postsAdapter;

    TextView bio, fullname;

    public interface ProfileClickHandler {
        void profileEditClicked();

        void accountViewClicked();

        void notificationCenterClicked();
    }

    public ProfileFragment() {
        current = this.getActivity();
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
        postsAdapter.setArgs(access_token);
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

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.notificationCenterClicked();
            }
        });

        tagsContainer.setOrientation(LinearLayoutManager.HORIZONTAL);

        tags.setHasFixedSize(true);
        tags.setLayoutManager(tagsContainer);
        tags.setAdapter(tagsAdapter);

        posts.setHasFixedSize(true);
        posts.setLayoutManager(postsContainer);
        posts.setAdapter(postsAdapter);

        // TODO add load more posts on scrolling.
        AndroidNetworking.get(getString(R.string.api_user))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addPathParameter(Constants.USERNAME, username)
                .build()
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

                                headerImage = object.getString(Constants.COVER_PHOTO);
                                Glide.with(header)
                                        .load(Constants.addAuthorization(getString(R.string.api_profile_photo) + headerImage, access_token))
                                        .into(header);

                                profileImage = object.getString(Constants.PROFILE_PHOTO);
                                Glide.with(image)
                                        .load(Constants.addAuthorization(getString(R.string.api_profile_photo) + profileImage, access_token))
                                        .into(image);

                                loadPosts();
                            } catch (JSONException e) {
                                Log.e(TAG, "Profile get info response was incomplete.");
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile get info request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });

        AndroidNetworking.get(getString(R.string.api_interests))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile get all interests response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Profile get all interests response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Profile get info response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONArray all = response.getJSONArray(Constants.INTERESTS);
                                for (int i = 0; i < all.length(); i++) {
                                    tagsAdapter.addTag(all.getJSONObject(i).getString(Constants.INTEREST));
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Profile get all interests response was incomplete.");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile get info request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });



        return view;
    }

    public void loadPosts() {
        AndroidNetworking.get(getString(R.string.api_posts))
                .addHeaders(Constants.AUTHORIZATION, access_token)
                .addQueryParameter(Constants.USERNAME, username)
                .addQueryParameter(Constants.PAGE, "1")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Posts get information response: " + response.toString());

                        String status = null;

                        try {
                            status = response.getString(Constants.STATUS);
                        } catch (JSONException e) {
                            Log.e(TAG, "Posts get information response JSONException: " + e.toString());
                        }

                        if (status == null) {
                            Log.e(TAG, "Posts get information response have no status parameter.");
                        } else if (status.equals(Constants.OK)) {
                            try {
                                JSONArray posts = response.getJSONArray(Constants.POSTS);
                                for (int i = 0; i < posts.length(); i++) {
                                    JSONObject post = posts.getJSONObject(i);

                                    postsAdapter.addPost(
                                            fullname.getText().toString(),
                                            post.getString(Constants.CREATED_DATE),  // TODO convert to difference of now and then.
                                            "some where",  // TODO get location.
                                            post.getString(Constants.LIKE_COUNT),
                                            post.getString(Constants.COMMENT_COUNT),
                                            post.getString(Constants.TEXT),
                                            getString(R.string.api_post_photo) + post.getString(Constants.PHOTO),
                                            getString(R.string.api_profile_photo) + profileImage
                                    );
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Posts get information response was incomplete.");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Profile get info request error: " + anError.toString() + " code: " + anError.getErrorCode());
                    }
                });
    }
}
