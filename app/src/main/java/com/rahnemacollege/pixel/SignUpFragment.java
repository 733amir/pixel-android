package com.rahnemacollege.pixel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    public interface SignUpFragmentClickHandler {
        public void signUpClicked(String fullname, String username, String password, String email);
        public RequestQueue getNetQ();
    }

    final String TAG = "SignUpFragment";

    SignUpFragmentClickHandler clickHandler;
    View view;
    EditText fullname, username, password_1, password_2, email;
    boolean unstat = false, fnstat = false, emstat = false, pwstat = false;

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
        // TODO check username existence with server at each character user types.

        view = inflater.inflate(R.layout.fragment_signup, container, false);
        fullname = view.findViewById(R.id.signup_et_fullname);

        username = view.findViewById(R.id.signup_et_username);
        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;

                String un = username.getText().toString();
                unstat = false;
                if (un.length() < 5) {
                    username.setError(getString(R.string.signup_short_string));
                } else if (!un.matches("[A-Za-z0-9]*")) {
                    username.setError(getString(R.string.signup_username_alphanumeric));
                } else {
                    String url = getString(R.string.api_exists) + "?username=" + un;
                    Log.i(TAG, "Username exists request url: " + url);
                    StringRequest stringReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.has("status") && res.get("status").equals("ok")) {
                                    username.setError(getString(R.string.signup_username_exists));
                                    unstat = false;
                                } else if (res.has("desc")) {
                                    username.setError(null);
                                    unstat = true;
                                } else {
                                    Log.e(TAG, "Username exists JSON response wasn't complete.");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Username exists response wasn't JSON.");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Error on username exists request: " + error.toString());
                        }
                    });

                    username.setError(null);
                    clickHandler.getNetQ().add(stringReq);
                }
            }
        });

        password_1 = view.findViewById(R.id.signup_et_password);
        password_1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;

                String pw = password_1.getText().toString();
                pwstat = false;
                if (pw.length() < 5) {
                    password_1.setError(getString(R.string.signup_short_string));
                } else {
                    password_1.setError(null);
                    pwstat = true;
                }

                pwstat = false;
                if (!pw.equals(password_2.getText().toString())) {
                    password_2.setError(getString(R.string.signup_password_not_matched));
                } else {
                    password_2.setError(null);
                    pwstat = true;
                }
            }
        });

        password_2 = view.findViewById(R.id.signup_et_repeat_password);
        password_2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;

                pwstat = false;
                if (!password_1.getText().toString().equals(password_2.getText().toString())) {
                    password_2.setError(getString(R.string.signup_password_not_matched));
                } else {
                    password_2.setError(null);
                    pwstat = true;
                }
            }
        });

        email = view.findViewById(R.id.signup_et_email);
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) return;

                String em = email.getText().toString();
                emstat = false;
                if (!isValidEmail(em)) {
                    email.setError(getString(R.string.signup_email_notvalid));
                } else {
                    email.setError(null);
                    emstat = true;
                }
            }
        });

        view.findViewById(R.id.signup_b_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unstr = username.getText().toString(), fnstr = fullname.getText().toString(),
                        emstr = email.getText().toString(), pwstr_1 = password_1.getText().toString(),
                        pwstr_2 = password_2.getText().toString();

                // Fields should not be empty.
                if (unstr.isEmpty()) {
                    username.setError(getString(R.string.signup_empty));
                    unstat = false;
                }
                if (fnstr.isEmpty()) {
                    fullname.setError(getString(R.string.signup_empty));
                    fnstat = false;
                } else {
                    fnstat = true;
                }
                if (emstr.isEmpty()) {
                    email.setError(getString(R.string.signup_empty));
                    emstat = false;
                }
                if (pwstr_1.isEmpty() || pwstr_2.isEmpty()) {
                    if (pwstr_1.isEmpty())
                        password_1.setError(getString(R.string.signup_empty));
                    if (pwstr_2.isEmpty())
                        password_2.setError(getString(R.string.signup_empty));
                    pwstat = false;
                }

                if (unstat && fnstat && emstat && pwstat) {
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

    // TODO show hide a textview error below each edittext
}
