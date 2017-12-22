package com.rahnemacollege.pixel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ResetPasswordActivity extends AppCompatActivity {

    final String TAG = "ResetPasswordActivity";

    EditText email, code, password_1, password_2;
    Button send_change, reset;
    String valid_email;
    RequestQueue netQ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        getSupportActionBar().setTitle(R.string.forget_password_password_recovery);

        email = findViewById(R.id.forget_password_et_email);
        send_change = findViewById(R.id.forget_password_b_send);
        code = findViewById(R.id.forget_password_et_code);
        password_1 = findViewById(R.id.forget_password_et_password);
        password_2 = findViewById(R.id.forget_password_et_password_repeat);
        reset = findViewById(R.id.forget_password_b_reset);
        valid_email = "";
        netQ = Volley.newRequestQueue(this);

        findViewById(R.id.forget_password_b_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid_email.isEmpty()) {
                    if (email.getText().toString().isEmpty()) {
                        email.setError(getString(R.string.forget_password_empty));
                        return;
                    } else if (!SignUpFragment.isValidEmail(email.getText().toString())) {
                        email.setError(getString(R.string.forget_password_email_notvalid));
                        return;
                    } else {
                        String url = getString(R.string.api_reset_request) + "?email=" + email.getText().toString();
                        final StringRequest emailExsitsReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i(TAG, "Email exists response is: " + response);

                                try {
                                    JSONObject res = new JSONObject(response);
                                    if (res.has("status") && res.get("status").equals("ok")) {
                                        valid_email = email.getText().toString();
                                        email.setError(null);
                                        email.setEnabled(false);
                                        send_change.setText(R.string.forget_password_change_email);
                                        findViewById(R.id.forget_password_container_inner).setVisibility(View.VISIBLE);
                                        code.setText("");
                                        code.setError(null);
                                        password_1.setText("");
                                        password_1.setError(null);
                                        password_2.setText("");
                                        password_2.setError(null);
                                    } else if (res.has("desc")) {
                                        email.setError(res.get("desc").toString());
                                    } else {
                                        Log.e(TAG, "Email exists JSON response wasn't complete.");
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "Email exists response wasn't JSON.");
                                } finally {
                                    findViewById(R.id.reset_password_page_loading).setVisibility(View.INVISIBLE);
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Email exists request error: " + error.toString());

                                findViewById(R.id.reset_password_page_loading).setVisibility(View.INVISIBLE);
                            }
                        });

                        findViewById(R.id.reset_password_page_loading).setVisibility(View.VISIBLE);
                        netQ.add(emailExsitsReq);
                    }
                } else {
                    valid_email = "";
                    email.setEnabled(true);
                    send_change.setText(R.string.forget_password_send);
                    findViewById(R.id.forget_password_container_inner).setVisibility(View.GONE);
                }
            }
        });

        findViewById(R.id.forget_password_b_reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean code_stat = false, pw_stat = false;

                if (code.getText().toString().isEmpty()) {
                    code.setError(getString(R.string.forget_password_empty));
                } else {
                    code_stat = true;
                }

                if (password_1.getText().toString().isEmpty() || password_2.getText().toString().isEmpty()) {
                    if (password_1.getText().toString().isEmpty())
                        password_1.setError(getString(R.string.forget_password_empty));

                    if (password_2.getText().toString().isEmpty())
                        password_2.setError(getString(R.string.forget_password_empty));
                } else if (!password_1.getText().toString().equals(password_2.getText().toString())) {
                    password_1.setError(getString(R.string.forget_password_password_not_matched));
                    password_2.setError(getString(R.string.forget_password_password_not_matched));
                } else {
                    pw_stat = true;
                }

                if (code_stat && pw_stat) {
                    findViewById(R.id.reset_password_page_loading).setVisibility(View.VISIBLE);
                    String url = getString(R.string.api_reset);
                    final StringRequest resetPassReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i(TAG, "Reset password response is: " + response);

                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.has("status") && res.get("status").equals("ok")) {
                                    email.setError(null);
                                    email.setEnabled(true);
                                    email.setText(null);
                                    send_change.setText(R.string.forget_password_send);
                                    findViewById(R.id.forget_password_container_inner).setVisibility(View.GONE);
                                    code.setText("");
                                    code.setError(null);
                                    password_1.setText("");
                                    password_1.setError(null);
                                    password_2.setText("");
                                    password_2.setError(null);

                                    passwordResetSuccessfully();
                                } else if (res.has("desc")) {
                                    code.setError(res.get("desc").toString());
                                } else {
                                    Log.e(TAG, "Reset password JSON response wasn't complete.");
                                }
                            } catch (JSONException e) {
                                Log.e(TAG, "Reset password response wasn't JSON.");
                            } finally {
                                findViewById(R.id.reset_password_page_loading).setVisibility(View.INVISIBLE);
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, "Reset password request error: " + error.toString());

                            findViewById(R.id.reset_password_page_loading).setVisibility(View.INVISIBLE);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();

                            params.put("email", valid_email);
                            params.put("code", code.getText().toString());
                            params.put("password", password_1.getText().toString());

                            return params;
                        }
                    };

                    findViewById(R.id.reset_password_page_loading).setVisibility(View.VISIBLE);
                    netQ.add(resetPassReq);
                }
            }
        });
    }

    public void passwordResetSuccessfully() {
        Toast.makeText(this, getString(R.string.forget_password_successful_reset), Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void finish() {
        if (Locale.getDefault().getDisplayLanguage().equals("English"))
            overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right);
        else
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
        super.finish();
    }
}
