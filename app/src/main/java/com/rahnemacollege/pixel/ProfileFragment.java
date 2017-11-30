package com.rahnemacollege.pixel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Button;



public class ProfileFragment extends Fragment implements View.OnClickListener {

    String TAG = "ProfileFragment";

    View view;
    ImageView profile_edit, notification, account_view;

    View mainView;
    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        profile_edit = view.findViewById(R.id.profile_edit);
        notification = view.findViewById(R.id.profile_notification);
        account_view = view.findViewById(R.id.profile_account);

        profile_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileEditActivity.class);
                startActivity(intent);
            }
        });

        account_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AccountEditActivity.class);
                startActivityForResult(intent, (short) R.integer.account_settings_request_code);
            }
        });


        Button postBtn=view.findViewById(R.id.uploadPost);
        postBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.uploadPost:
                startActivity(new Intent(getActivity(),UploadPost.class));
                break;
        }
    }
}
