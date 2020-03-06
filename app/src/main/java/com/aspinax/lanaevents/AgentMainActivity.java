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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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
                switch (resultCode) {
                    case 0:
                        p = (Agent) result.get(mAuth.getUid());
                        Toast.makeText(getApplicationContext(), p.orgId, Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
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
                        AgentProfileFragment profileFragment = new AgentProfileFragment();
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
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        }
    }
}