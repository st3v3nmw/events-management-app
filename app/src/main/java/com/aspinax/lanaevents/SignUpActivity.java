package com.aspinax.lanaevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;

import static com.aspinax.lanaevents.LoginActivity.isValidEmail;
import static com.aspinax.lanaevents.LoginActivity.isValidPassword;

public class SignUpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final MaterialButton sign_up_btn = findViewById(R.id.sign_up_btn);
        final TextInputEditText fullnameView = findViewById(R.id.full_name);
        final TextInputEditText emailView = findViewById(R.id.email);
        final TextInputEditText passwordView = findViewById(R.id.password);

        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullname = fullnameView.getText().toString().trim();
                final String email = emailView.getText().toString().trim();
                final String password = passwordView.getText().toString().trim();

                if (!TextUtils.isEmpty(fullname)) {
                    if (isValidEmail(email)) {
                        if (isValidPassword(password)) {
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                Person.saveUser(mAuth, fullname, email, FieldValue.serverTimestamp());
                                                Intent intent = new Intent(SignUpActivity.this, UserMainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(), getString(R.string.signup_failed), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            passwordView.setError(getString(R.string.password_val));
                        }
                    } else {
                        emailView.setError(getString(R.string.email_val));
                    }
                } else {
                    fullnameView.setError(getString(R.string.fname_val));
                }
            }
        });
    }
}