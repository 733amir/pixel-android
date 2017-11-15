package com.rahnemacollege.pixel;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends Fragment {
    OnForgetPasswordClicked mCallback;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        view.findViewById(R.id.login_tv_forgetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.forgetPasswordClicked();
            }
        });

        view.findViewById(R.id.login_b_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText username = view.findViewById(R.id.login_et_username);
                EditText password = view.findViewById(R.id.login_et_password);

                mCallback.loginClicked(username.getText().toString(), password.getText().toString());
            }
        });

        return view;
    }

    // Container Activity must implement this interface
    public interface OnForgetPasswordClicked {
        public void forgetPasswordClicked();
        public void loginClicked(String username, String password);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnForgetPasswordClicked) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnForgetPasswordClicked.forgetPasswordClicked interface.");
        }
    }
}
