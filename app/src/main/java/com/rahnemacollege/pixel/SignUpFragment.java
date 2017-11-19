package com.rahnemacollege.pixel;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpFragment extends Fragment {

    public interface SignUpFragmentClickHandler {
        public void signUpClicked(String fullname, String username, String password, String email);
    }

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
                boolean unstat = false, fnstat = false, emstat = false, pwstat = false;

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

                // Full Name field checks
                if (fnstr.isEmpty()) {
                    fullname.setError(getString(R.string.signup_empty));
                } else {
                    fnstat = true;
                }

                // Email field checks
                if (emstr.isEmpty()) {
                    email.setError(getString(R.string.signup_empty));
                } else if (!isValidEmail(emstr)) {
                    email.setError(getString(R.string.signup_email_notvalid));
                } else {
                    emstat = true;
                }

                // Password fields checks
                if (pwstr_1.isEmpty() || pwstr_2.isEmpty()) {
                    if (pwstr_1.isEmpty())
                        password_1.setError(getString(R.string.signup_empty));
                    if (pwstr_2.isEmpty())
                        password_2.setError(getString(R.string.signup_empty));
                } else if (!pwstr_1.equals(pwstr_2)) {
                    password_1.setError(getString(R.string.signup_password_not_matched));
                    password_2.setError(getString(R.string.signup_password_not_matched));
                } else {
                    pwstat = true;
                }

                if (unstat && fnstat && emstat && pwstat) {
                    // TODO show a loading animation
                    // TODO send registration to server and wait for response. if it was ok switch to login page.
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
}
