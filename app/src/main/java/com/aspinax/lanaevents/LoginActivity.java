package com.aspinax.lanaevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final MaterialButton login_btn = findViewById(R.id.login_btn);
        final TextInputEditText emailView = findViewById(R.id.email);
        final TextInputEditText passwordView = findViewById(R.id.password);
        final MaterialButton gmail_login = findViewById(R.id.gmail_login);
        final TextView forgotpass = findViewById(R.id.forgot_pass);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailView.getText().toString().trim();
                final String password = passwordView.getText().toString().trim();

                if (isValidEmail(email)) {
                    if (isValidPassword(password)) {
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        passwordView.setError(getString(R.string.password_val));
                    }
                } else {
                    emailView.setError(getString(R.string.email_val));
                }
            }
        });

        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        gmail_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static boolean isValidEmail(CharSequence email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidPassword(CharSequence password) {
        return (!TextUtils.isEmpty(password) && password.length() > 8);
    }
}