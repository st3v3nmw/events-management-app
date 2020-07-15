package com.aspinax.lanaevents;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;


public class AgentHomeFragment extends Fragment {
    private Person p;
    public AgentHomeFragment() {}

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_agent_home, container,
                false);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        final TextView helloFriend = view.findViewById(R.id.hello_friend);

        assert user != null;
        Database db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    p = (Person) result.get(mAuth.getUid());
                    assert p != null;
                    helloFriend.setText("Hey " + p.getFullName() + ",");
                    p.setAuth(mAuth);
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {

            }
        });

        db.read("users", user.getUid(), Person.class, 0);
        return view;
    }
}