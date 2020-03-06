package com.aspinax.lanaevents;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class EventsAdapter extends ArrayAdapter<Event> {
    public EventsAdapter(Context context, List<Event> object) {
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

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookEventBottomSheet bookEventBottomSheet = new BookEventBottomSheet(event.name, mAuth.getUid(), event.eventId);
                bookEventBottomSheet.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "Book Event");
            }
        });
        return convertView;
    }
}