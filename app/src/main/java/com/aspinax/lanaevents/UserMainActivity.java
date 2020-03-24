package com.aspinax.lanaevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class UserMainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Database db;
    private Person p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);
        mAuth = FirebaseAuth.getInstance();
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    p = (Person) result.get(mAuth.getUid());
                    assert p != null;
                    if (p.access > 0) {
                        Intent intent = new Intent(UserMainActivity.this, AgentMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    p.setAuth(mAuth);
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        setDiscoverFragment();
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.discover:
                        setDiscoverFragment();
                        break;
                    case R.id.myevents:
                        UserEventsFragment eventsFragment = new UserEventsFragment();
                        fragmentTransaction.replace(R.id.content, eventsFragment);
                        fragmentTransaction.commit();
                        break;
                    case R.id.profile:
                        ProfileFragment profileFragment = new ProfileFragment();
                        fragmentTransaction.replace(R.id.content, profileFragment);
                        fragmentTransaction.commit();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), NoAuthActivity.class));
            finish();
        } else {
            db.read("users", currentUser.getUid(), Person.class, 0);
        }
    }

    private void setDiscoverFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UserDiscoverFragment discoverFragment = new UserDiscoverFragment();
        fragmentTransaction.replace(R.id.content, discoverFragment);
        fragmentTransaction.commit();
    }

    public static Bitmap decodeBase64(String base64String) {
        base64String = base64String.substring(base64String.indexOf(",") + 1);
        final byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}