package com.aspinax.lanaevents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

                    finalConvertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), ViewEventActivity.class);
                            intent.putExtra("eventId", ticket.eventId);
                            intent.putExtra("name", event.name);
                            intent.putExtra("attendeeCount", event.attendeeCount);
                            intent.putExtra("end", event.end.getSeconds());
                            intent.putExtra("location", event.location);
                            intent.putExtra("orgId", event.orgId);
                            intent.putExtra("start", event.start.getSeconds());
                            intent.putExtra("type", event.type);
                            intent.putExtra("image", event.image);
                            intent.putExtra("hearts", event.hearts);
                            intent.putExtra("addedBy", event.addedBy);
                            intent.putExtra("checkInCount", event.checkInCount);
                            getContext().startActivity(intent);
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