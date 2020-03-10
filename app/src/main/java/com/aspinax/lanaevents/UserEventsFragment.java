package com.aspinax.lanaevents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class UserEventsFragment extends Fragment {
    private Database db;
    public UserEventsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_events, container, false);
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    List<Ticket> ticketList = new ArrayList<>();
                    for (String ticketId: result.keySet()) {
                        Ticket ticket = (Ticket) result.get(ticketId);
                        assert ticket != null;
                        ticket.setTicketId(ticketId);
                        ticketList.add(ticket);
                    }
                    ListView eventsListView = view.findViewById(R.id.eventsList);
                    MyEventsAdapter eventsAdapter = new MyEventsAdapter(getContext(), ticketList);
                    eventsListView.setAdapter(eventsAdapter);
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db.filterWithOneField("tickets", "userId", mAuth.getUid(), Ticket.class, 0);
        return view;
    }
}