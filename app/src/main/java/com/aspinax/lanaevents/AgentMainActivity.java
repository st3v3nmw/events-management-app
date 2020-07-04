package com.aspinax.lanaevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;
import java.util.Map;

public class AgentMainActivity extends AppCompatActivity {
    private Database db;
    private FirebaseAuth mAuth;
    private Agent p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_main);

        mAuth = FirebaseAuth.getInstance();
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    p = (Agent) result.get(mAuth.getUid());
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                if (resultCode == 1) {
                    AlertDialog checkInDialog = new AlertDialog();
                    checkInDialog.showDialog(AgentMainActivity.this, msg, true);
                    if (msg.equals("Success")) {
                        checkInDialog.showDialog(AgentMainActivity.this, "Check In Successful", false);
                    } else {
                        checkInDialog.showDialog(AgentMainActivity.this, "Check In Failed", true);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                }
            }
        });

        db.read("users", mAuth.getUid(), Agent.class, 0);
        setHomeFragment();
        BottomNavigationView bottomNav = findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                switch(item.getItemId()) {
                    case R.id.home:
                        setHomeFragment();
                        break;
                    case R.id.scan:
                        IntentIntegrator integrator = new IntentIntegrator(AgentMainActivity.this);
                        integrator.setPrompt("Align the QR Code");
                        integrator.setCameraId(0);
                        integrator.setBeepEnabled(false);
                        integrator.setBarcodeImageEnabled(true);
                        integrator.setCaptureActivity(CaptureActivityPortrait.class);
                        integrator.initiateScan();
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

    private void setHomeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        AgentHomeFragment homeFragment = new AgentHomeFragment();
        fragmentTransaction.replace(R.id.content, homeFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Map<String, Object> checkIn = new HashMap<>();
                checkIn.put("checkInBy", mAuth.getUid());
                checkIn.put("time", FieldValue.serverTimestamp());
                db.addToSubCollection("tickets", result.getContents(), "checkIns", checkIn, 1);
            }
        }
    }
}