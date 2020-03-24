package com.aspinax.lanaevents;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.aspinax.lanaevents.LoginActivity.isValidEmail;
import static com.aspinax.lanaevents.LoginActivity.isValidPassword;


public class ProfileFragment extends Fragment {
    private Database db;
    private FirebaseAuth mAuth;
    private Person p;

    public ProfileFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        final TextInputEditText fNameView = view.findViewById(R.id.fName);
        final TextInputEditText lNameView = view.findViewById(R.id.lName);
        final TextInputEditText emailView = view.findViewById(R.id.email);
        final TextInputEditText phoneView = view.findViewById(R.id.phoneNumber);
        final MaterialButton saveContact = view.findViewById(R.id.saveContact);
        final MaterialButton saveName = view.findViewById(R.id.saveName);

        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    p = (Person) result.get(mAuth.getUid());
                    assert p != null;
                    fNameView.setText(p.fName);
                    fNameView.setEnabled(true);
                    lNameView.setText(p.lName);
                    lNameView.setEnabled(true);
                    saveName.setEnabled(true);
                    emailView.setText(p.email);
                    emailView.setEnabled(true);
                    phoneView.setText(p.phoneNumber);
                    phoneView.setEnabled(true);
                    saveContact.setEnabled(true);
                } else if (resultCode == 1) {
                    Toast.makeText(getContext(), "Profile Updated.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        db.read("users", mAuth.getUid(), Agent.class, 0);

        MaterialButton logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), NoAuthActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        saveName.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String fName = Objects.requireNonNull(fNameView.getText()).toString().trim();
                String lName = Objects.requireNonNull(lNameView.getText()).toString().trim();

                if (TextUtils.isEmpty(fName)) {
                    fNameView.setError(getString(R.string.fname_val));
                    return;
                }
                if (TextUtils.isEmpty(lName)) {
                    lNameView.setError("Please enter your last name.");
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("fName", fName);
                data.put("lName", lName);
                db.update("users", mAuth.getUid(), data, 1);
            }
        });

        saveContact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = Objects.requireNonNull(emailView.getText()).toString().trim();
                String phoneNumber = Objects.requireNonNull(phoneView.getText()).toString().trim();

                if (!isValidEmail(email)) {
                    emailView.setError(getString(R.string.email_val));
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneView.setError(getString(R.string.password_val));
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("email", email);
                data.put("phoneNumber", phoneNumber);
                db.update("users", mAuth.getUid(), data, 1);
            }
        });

        final TextInputEditText oldPasswordView = view.findViewById(R.id.oldPassword);
        final TextInputEditText newPasswordView = view.findViewById(R.id.newPassword);
        MaterialButton savePassword = view.findViewById(R.id.savePassword);
        savePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String oldPassword = Objects.requireNonNull(oldPasswordView.getText()).toString().trim();
                String newPassword = Objects.requireNonNull(newPasswordView.getText()).toString().trim();

                if (!isValidPassword(oldPassword)) {
                    oldPasswordView.setError(getString(R.string.password_val));
                    return;
                }
                if (!isValidPassword(newPassword)) {
                    newPasswordView.setError(getString(R.string.password_val));
                    return;
                }

                // update password
            }
        });
        return view;
    }
}