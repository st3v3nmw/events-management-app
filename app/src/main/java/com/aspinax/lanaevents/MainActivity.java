package com.aspinax.lanaevents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            notLoggedIn();
        } else {
            Database db = new Database(new AsyncResponse() {
                @Override
                public void resultHandler(Map<String, Object> result, int resultCode) {
                    Person p = (Person) result.get(mAuth.getUid());
                    if (p == null) {
                        notLoggedIn();
                    } else {
                        Intent intent;
                        if (p.access > 0) intent = new Intent(MainActivity.this, AgentMainActivity.class);
                        else intent = new Intent(MainActivity.this, UserMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }

                @Override
                public void resultHandler(String msg, int resultCode) {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            });

            db.read("users", mAuth.getUid(), Person.class, 0);
        }
    }

    private void notLoggedIn() {
        Intent intent = new Intent(this, NoAuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}