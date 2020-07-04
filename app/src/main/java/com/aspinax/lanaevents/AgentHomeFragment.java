package com.aspinax.lanaevents;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;
import java.util.Objects;


public class AgentHomeFragment extends Fragment {
    private Database db;
    private Person p;
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
        final TextView eventNameView = view.findViewById(R.id.event_name);
        final TextView eventLocationView = view.findViewById(R.id.event_location);
        final ImageView bannerImage = view.findViewById(R.id.banner_image);
        assert user != null;
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    p = (Person) result.get(mAuth.getUid());
                    assert p != null;
                    helloFriend.setText("Hey " + p.getFullName() + ",");
                    p.setAuth(mAuth);
                } else if (resultCode == 1) {
                    assert result != null;
                    for (String eventId: result.keySet()) {
                        event = (Event) result.get(eventId);
                        assert event != null;
                        if (event.start.getSeconds() < Timestamp.now().getSeconds()) {
                            Log.e("e", event.name);
                            bannerImage.setImageBitmap(event.imageBitmap);
                            eventNameView.setText(event.name);
                            eventLocationView.setText(event.location);
                            break;
                        }
                    }
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {

            }
        });

        db.read("users", user.getUid(), Person.class, 0);
        db.compareGreater("events", "end", Timestamp.now(), Event.class, 1);

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