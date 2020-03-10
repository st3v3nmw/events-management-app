package com.aspinax.lanaevents;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDiscoverFragment extends Fragment {
    private Database db;
    public UserDiscoverFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_discover, container, false);
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    List<Event> eventList = new ArrayList<>();
                    for (String eventId: result.keySet()) {
                        Event event = (Event) result.get(eventId);
                        assert event != null;
                        event.setEventId(eventId);
                        eventList.add(event);
                    }
                    ListView eventsListView = view.findViewById(R.id.eventsList);
                    EventsAdapter eventsAdapter = new EventsAdapter(getContext(), eventList);
                    eventsListView.setAdapter(eventsAdapter);
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        db.readCollection("events", Event.class, 0);
        return view;
    }
}