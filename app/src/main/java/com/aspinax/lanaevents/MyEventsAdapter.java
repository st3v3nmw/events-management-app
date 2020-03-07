package com.aspinax.lanaevents;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Map;

public class MyEventsAdapter extends ArrayAdapter<Ticket> {
    private Database db;
    public MyEventsAdapter(Context context, List<Ticket> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_event, parent, false);
        }

        final TextView event_nameView = convertView.findViewById(R.id.event_name);
        final TextView event_locationView = convertView.findViewById(R.id.event_location);
        final ImageView banner_image = convertView.findViewById(R.id.banner_image);

        final Ticket ticket = getItem(position);
        assert ticket != null;

        final View finalConvertView = convertView;
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    final Event event = (Event) result.get(ticket.eventId);
                    assert event != null;
                    event_nameView.setText(event.name);
                    event_locationView.setText(event.location);
                    banner_image.setImageBitmap(event.imageBitmap);

                    final FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    finalConvertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TicketBottomSheet ticketBottomSheet = new TicketBottomSheet(event, ticket);
                            ticketBottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "View Ticket");
                        }
                    });
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        db.read("events", ticket.eventId, Event.class, 0);
        return convertView;
    }
}