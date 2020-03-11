package com.aspinax.lanaevents;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView event_nameView, startView;
        ImageView banner_image;

        ViewHolder(View convertView) {
            super(convertView);
            event_nameView = convertView.findViewById(R.id.event_name);
            startView = convertView.findViewById(R.id.event_start);
            banner_image = convertView.findViewById(R.id.banner_image);
        }
    }

    private List<Event> eventList;
    private Context mContext;

    EventsAdapter(Context context, List<Event> eventList) {
        this.eventList = eventList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eItemView = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(eItemView);
    }

    @Override
    public void onBindViewHolder(EventsAdapter.ViewHolder viewHolder, int position) {
        final Event event = eventList.get(position);
        assert event != null;
        viewHolder.event_nameView.setText(event.name);
        SimpleDateFormat startDate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
        viewHolder.startView.setText(startDate.format(event.start.toDate()));
        viewHolder.banner_image.setImageBitmap(event.imageBitmap);

        viewHolder.banner_image.setOnClickListener(new View.OnClickListener() {
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
                intent.putExtra("addedBy", event.addedBy);
                intent.putExtra("checkInCount", event.checkInCount);
                intent.putExtra("longitude", event.coordinates.get("longitude"));
                intent.putExtra("latitude", event.coordinates.get("latitude"));
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}