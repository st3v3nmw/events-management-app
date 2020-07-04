package com.aspinax.lanaevents;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Objects;


public class AgentHomeFragment extends Fragment {
    private Database db;
    private Event event;
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
        helloFriend.setText("Hey " + user.getDisplayName() + ",");
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {

            }

            @Override
            public void resultHandler(String msg, int resultCode) {

            }
        });

        MaterialButton attendanceBtn = view.findViewById(R.id.attendanceListBtn);
        attendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonListDialogFragment.newInstance(event).show(Objects.requireNonNull(getActivity())
                        .getSupportFragmentManager(), "dialog");
            }
        });
        return view;
    }
}