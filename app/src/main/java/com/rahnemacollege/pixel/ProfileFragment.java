package com.rahnemacollege.pixel;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import co.lujun.androidtagview.TagContainerLayout;
import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    String TAG = "ProfileFragment";

    View view;
    ImageView profile_edit, notification, account_view, header;
    CircleImageView image;
    ProfileClickHandler clickHandler;
    String username;
    TagContainerLayout tags;
    TextView bio, fullname;

    public interface ProfileClickHandler {
        public void profileEditClicked();
        public void accountViewClicked();
    }

    public ProfileFragment() {
    }

    public void setArgs(String username) {
        this.username = username;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_edit = view.findViewById(R.id.profile_edit);
        notification = view.findViewById(R.id.profile_notification);
        account_view = view.findViewById(R.id.profile_account);
        clickHandler = (ProfileClickHandler) getActivity();
        header = view.findViewById(R.id.profile_header_container);
        image = view.findViewById(R.id.profile_image_container);
        tags = view.findViewById(R.id.profile_tags);
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

        AndroidNetworking.get(getString(R.string.api_profile))
                .addQueryParameter("username", username)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i(TAG, "Profile get information response: " + response.toString());
                        try {
                            if (response.get("status").equals("ok")) {
                                JSONObject profile = new JSONObject(response.get("profile").toString());

                                Glide.with(getActivity()).load(profile.getString("header")).into(header);
                                Glide.with(getActivity()).load(profile.getString("image")).into(image);

                                JSONArray interests = profile.getJSONArray("interest");
                                for (int i = 0; i < interests.length(); i++) {
                                    tags.addTag(interests.getString(i));
                                }

                                bio.setText(profile.getString("bio"));

                                fullname.setText(response.getString("fullname"));
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
