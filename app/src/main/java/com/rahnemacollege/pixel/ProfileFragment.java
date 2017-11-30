package com.rahnemacollege.pixel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ProfileFragment extends Fragment {

    String TAG = "ProfileFragment";

    View view;
    ImageView profile_edit, notification, account_view;
    ProfileClickHandler clickHandler;

    public interface ProfileClickHandler {
        public void profileEditClicked();
        public void accountViewClicked();
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_profile, container, false);
         profile_edit = view.findViewById(R.id.profile_edit);
         notification = view.findViewById(R.id.profile_notification);
         account_view = view.findViewById(R.id.profile_account);
         clickHandler = (ProfileClickHandler) getActivity();

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

         return view;
    }
}
