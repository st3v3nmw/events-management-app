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
import java.util.Map;

public class MyEventsAdapter extends RecyclerView.Adapter<MyEventsAdapter.ViewHolder> {
    private Database db;

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

    private List<Ticket> ticketList;
    private Context mContext;

    MyEventsAdapter(Context context, List<Ticket> ticketList) {
        this.ticketList = ticketList;
        this.mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public MyEventsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View eItemView = inflater.inflate(R.layout.item_event, parent, false);
        return new ViewHolder(eItemView);
    }

    @Override
    public void onBindViewHolder(final MyEventsAdapter.ViewHolder viewHolder, int position) {
        final Ticket ticket = ticketList.get(position);
        assert ticket != null;
        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    final Event event = (Event) result.get(ticket.eventId);
                    assert event != null;
                    viewHolder.event_nameView.setText(event.name);
                    SimpleDateFormat startDate = new SimpleDateFormat("dd MMMM yyyy", Locale.US);
                    viewHolder.startView.setText(startDate.format(event.start.toDate()));
                    viewHolder.banner_image.setImageBitmap(event.imageBitmap);

                    viewHolder.banner_image.setOnClickListener(new View.OnClickListener() {
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
                            intent.putExtra("addedBy", event.addedBy);
                            intent.putExtra("checkInCount", event.checkInCount);
                            intent.putExtra("longitude", event.coordinates.get("longitude"));
                            intent.putExtra("latitude", event.coordinates.get("latitude"));
                            getContext().startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {

            }
        });

        db.read("events", ticket.eventId, Event.class, 0);
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }
}