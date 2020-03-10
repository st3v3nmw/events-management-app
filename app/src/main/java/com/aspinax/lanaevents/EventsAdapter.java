package com.aspinax.lanaevents;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {
    EventsAdapter(Context context, List<Event> object) {
        super(context, 0, object);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_event, parent, false);
        }
        TextView event_nameView = convertView.findViewById(R.id.event_name);
        TextView event_locationView = convertView.findViewById(R.id.event_location);
        ImageView banner_image = convertView.findViewById(R.id.banner_image);
        final Event event = getItem(position);
        assert event != null;
        event_nameView.setText(event.name);
        event_locationView.setText(event.location);
        banner_image.setImageBitmap(event.imageBitmap);

        final View finalConvertView = convertView;
        finalConvertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ViewEventActivity.class);
                intent.putExtra("eventId", event.eventId);
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

        return convertView;
    }
}