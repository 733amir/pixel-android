package com.rahnemacollege.pixel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ResetPasswordActivity extends AppCompatActivity {

    EditText email, code, password_1, password_2;
    Button send_change, reset;
    String valid_email;

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
                        valid_email = email.getText().toString();
                        // TODO loading animations.
                        // TODO check with server if this email address exist. server should send email with a code.
                    }

                    email.setEnabled(false);

                    send_change.setText(R.string.forget_password_change_email);

                    findViewById(R.id.forget_password_container_inner).setVisibility(View.VISIBLE);
                    code.setText("");
                    code.setError(null);
                    password_1.setText("");
                    password_1.setError(null);
                    password_2.setText("");
                    password_2.setError(null);
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
                    // TODO send data to server for password reset.
                }
            }
        });
    }
}
