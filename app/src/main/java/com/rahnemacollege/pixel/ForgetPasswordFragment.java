package com.rahnemacollege.pixel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ForgetPasswordFragment extends Fragment {

    View view;
    EditText email, code, password_1, password_2;
    Button send_change, reset;
    String valid_email;

    public ForgetPasswordFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        email = view.findViewById(R.id.forget_password_et_email);
        send_change = view.findViewById(R.id.forget_password_b_send);
        code = view.findViewById(R.id.forget_password_et_code);
        password_1 = view.findViewById(R.id.forget_password_et_password);
        password_2 = view.findViewById(R.id.forget_password_et_password_repeat);
        reset = view.findViewById(R.id.forget_password_b_reset);
        valid_email = "";

        view.findViewById(R.id.forget_password_b_send).setOnClickListener(new View.OnClickListener() {
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

                    view.findViewById(R.id.forget_password_container_inner).setVisibility(View.VISIBLE);
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

                    view.findViewById(R.id.forget_password_container_inner).setVisibility(View.GONE);
                }
            }
        });

        view.findViewById(R.id.forget_password_b_reset).setOnClickListener(new View.OnClickListener() {
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

        return view;
    }

}
