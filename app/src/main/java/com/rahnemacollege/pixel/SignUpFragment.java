package com.rahnemacollege.pixel;


import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUpFragment extends Fragment {

    public interface SignUpFragmentClickHandler {
        public void signUpClicked(String fullname, String username, String password, String email);
    }

    final String TAG = "SignUpFragment";

    SignUpFragmentClickHandler clickHandler;
    View view;
    EditText fullname, username, password_1, password_2, email;

    public SignUpFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        clickHandler = ((SignUpFragmentClickHandler) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_signup, container, false);
        fullname = view.findViewById(R.id.signup_et_fullname);
        username = view.findViewById(R.id.signup_et_username);
        password_1 = view.findViewById(R.id.signup_et_password);
        password_2 = view.findViewById(R.id.signup_et_repeat_password);
        email = view.findViewById(R.id.signup_et_email);

        view.findViewById(R.id.signup_b_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unstr = username.getText().toString(), fnstr = fullname.getText().toString(),
                        emstr = email.getText().toString(), pwstr_1 = password_1.getText().toString(),
                        pwstr_2 = password_2.getText().toString();

                boolean unstat = false, fnstat = false, emstat = false, pw1stat = false, pw2stat = false;

                if (unstr.isEmpty()) {
                    username.setError(getString(R.string.signup_empty));
                } else if (unstr.length() < 5) {
                    username.setError(getString(R.string.signup_short_string));
                } else if (!unstr.matches("[A-Za-z0-9]*")) {
                    username.setError(getString(R.string.signup_username_alphanumeric));
                } else {
                    username.setError(null);
                    unstat = true;
                }

                if (fnstr.isEmpty()) {
                    fullname.setError(getString(R.string.signup_empty));
                } else {
                    fullname.setError(null);
                    fnstat = true;
                }

                if (emstr.isEmpty()) {
                    email.setError(getString(R.string.signup_empty));
                } else if (!isValidEmail(emstr)) {
                    email.setError(getString(R.string.signup_email_notvalid));
                } else {
                    email.setError(null);
                    emstat = true;
                }

                if (pwstr_1.isEmpty()) {
                    password_1.setError(getString(R.string.signup_empty));
                } else if (pwstr_1.length() < 5 ) {
                    password_1.setError(getString(R.string.signup_short_string));
                } else {
                    password_1.setError(null);
                    pw1stat = true;
                }

                if (pwstr_2.isEmpty()) {
                    password_2.setError(getString(R.string.signup_empty));
                } else if (!pwstr_2.equals(pwstr_1)) {
                    password_2.setError(getString(R.string.signup_password_not_matched));
                } else {
                    password_2.setError(null);
                    pw2stat = true;
                }

                if (unstat && fnstat && emstat && pw1stat && pw2stat) {
                    clickHandler.signUpClicked(fullname.getText().toString(),
                            username.getText().toString(), password_1.getText().toString(),
                            email.getText().toString());
                }
            }
        });

        return view;
    }

    public static boolean isValidEmail(CharSequence target) {
        return target != null && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public void resetAll() {
        username.setText(null);
        username.setError(null);
        fullname.setText(null);
        fullname.setError(null);
        email.setText(null);
        email.setError(null);
        password_1.setText(null);
        password_1.setError(null);
        password_2.setText(null);
        password_2.setError(null);
    }
}

