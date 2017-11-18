package com.rahnemacollege.pixel;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class LoginFragment extends Fragment {

    public interface LoginFragmentClickHandler {
        public void forgetPasswordClicked();
        public void loginClicked(String username, String password);
    }

    LoginFragmentClickHandler clickHandler;
    View view;
    EditText username, password;

    public LoginFragment() {}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clickHandler = ((LoginFragmentClickHandler)getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);
        username = view.findViewById(R.id.login_et_username);
        password = view.findViewById(R.id.login_et_password);

        view.findViewById(R.id.login_tv_forgetpassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHandler.forgetPasswordClicked();
            }
        });

        view.findViewById(R.id.login_b_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unstr = username.getText().toString(), pwstr = password.getText().toString();
                boolean unstat = false, pwstat = false;

                // Username field checks
                if (unstr.isEmpty()) {
                    username.setError(getString(R.string.signup_empty));
                } else if (unstr.length() < 5) {
                    username.setError(getString(R.string.signup_short_username));
                } else if (!unstr.matches("[A-Za-z0-9]*")) {
                    username.setError(getString(R.string.signup_username_alphanumeric));
                } // TODO check username with server.
                else {
                    unstat = true;
                }

                // Password field checks
                if (pwstr.isEmpty()) {
                    password.setError(getString(R.string.login_empty));
                } else {
                    pwstat = true;
                }

                if (unstat && pwstat) {
                    clickHandler.loginClicked(username.getText().toString(), password.getText().toString());
                }
            }
        });

        return view;
    }

    public void fillUsernamePassword(String un, String pw) {
        username.setText(un);
        password.setText(pw);
    }
}