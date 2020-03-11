package com.aspinax.lanaevents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.aspinax.lanaevents.UserDiscoverFragment.getTodayDate;

public class UserEventsFragment extends Fragment {
    private Database db;
    public UserEventsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_events, container, false);
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                List<Ticket> ticketList = new ArrayList<>();
                for (String ticketId: result.keySet()) {
                    Ticket ticket = (Ticket) result.get(ticketId);
                    assert ticket != null;
                    ticket.setTicketId(ticketId);
                    ticketList.add(ticket);
                }

                if (resultCode == 0) {
                    Collections.sort(ticketList);
                    MyEventsAdapter eventsAdapter = new MyEventsAdapter(getContext(), ticketList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    RecyclerView eventsListView = view.findViewById(R.id.eventsList);
                    eventsListView.setLayoutManager(layoutManager);
                    if (getContext() != null) {
                        eventsListView.setAdapter(eventsAdapter);
                    }
                    db.filterWithOneFieldAndCompareLess("tickets", "userId", mAuth.getUid(), "end", getTodayDate(), Ticket.class, 1);
                } else {
                    Collections.sort(ticketList, Collections.<Ticket>reverseOrder());
                    MyEventsAdapter eventsAdapter = new MyEventsAdapter(getContext(), ticketList);
                    LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    RecyclerView  pastEventsListView = view.findViewById(R.id.pastEventsList);
                    pastEventsListView.setLayoutManager(layoutManager2);
                    if (getContext() != null) {
                        pastEventsListView.setAdapter(eventsAdapter);
                    }
                }
                }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });

        db.filterWithOneFieldAndCompareGreater("tickets", "userId", mAuth.getUid(), "end", getTodayDate(), Ticket.class,0);
        return view;
    }
}