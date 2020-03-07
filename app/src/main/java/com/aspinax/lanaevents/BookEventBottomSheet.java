package com.aspinax.lanaevents;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;


public class BookEventBottomSheet extends BottomSheetDialogFragment {
    private String event_name, userId, eventId;
    private Database db;

    public BookEventBottomSheet(String event, String userId, String eventId) {
        this.event_name = event;
        this.userId = userId;
        this.eventId = eventId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView =View.inflate(getContext(), R.layout.fragment_book_event_bottom_sheet, null);
        TextView event_namebsView = contentView.findViewById(R.id.event_namebs);
        event_namebsView.setText(this.event_name);
        dialog.setContentView(contentView);
        final MaterialButton book = contentView.findViewById(R.id.book_btn);

        db = new Database(new AsyncResponse() {
            @Override
            public void resultHandler(Map<String, Object> result, int resultCode) {
                if (resultCode == 0) {
                    if (result.isEmpty()) {
                        book.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Map<String, Object> data = new HashMap<>();
                                data.put("eventId", BookEventBottomSheet.this.eventId);
                                data.put("userId", BookEventBottomSheet.this.userId);
                                data.put("createdAt", FieldValue.serverTimestamp());
                                db.add("tickets", data, 1);
                            }
                        });
                    } else {
                        markAsBooked(book);
                    }
                }
            }

            @Override
            public void resultHandler(String msg, int resultCode) {
                if (resultCode == 1) {
                    markAsBooked(book);
                } else {
                     Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
                 }
            }
        });
        db.filterWithTwoFields("tickets", "userId", this.userId, "eventId", this.eventId, Ticket.class, 0);
    }

    private void markAsBooked(MaterialButton book) {
        book.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.red));
        book.setText("Booked");
    }
}