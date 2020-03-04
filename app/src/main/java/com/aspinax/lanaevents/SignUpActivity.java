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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

import static com.aspinax.lanaevents.LoginActivity.isValidEmail;
import static com.aspinax.lanaevents.LoginActivity.isValidPassword;

public class SignUpActivity extends AppCompatActivity {
    final Database db = new Database(new AsyncResponse() {
        @Override
        public void resultHandler(Map<String, Object> result, int resultCode) {

        }

        @Override
        public void resultHandler(String msg) {
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        final MaterialButton sign_up_btn = findViewById(R.id.sign_up_btn);
        final MaterialButton gmail_signup = findViewById(R.id.gmail_sign_up);
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
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(fullname)
                                                        .build();

                                                user.updateProfile(profileUpdate)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {}
                                                        });

                                                String[] names = fullname.split(" ");
                                                Map<String, Object> data = new HashMap<>();
                                                data.put("fName", names[0]);
                                                data.put("lName", names[1]);
                                                data.put("email", email);
                                                data.put("created", FieldValue.serverTimestamp());
                                                data.put("access", 0);
                                                db.insert("users", user.getUid(), data);
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
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

        gmail_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
