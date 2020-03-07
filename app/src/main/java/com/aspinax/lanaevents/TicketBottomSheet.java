package com.aspinax.lanaevents;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;


public class TicketBottomSheet extends BottomSheetDialogFragment {
    private Event event;
    private Ticket ticket;

    public TicketBottomSheet(Event event, Ticket ticket) {
        this.event = event;
        this.ticket = ticket;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        View contentView =View.inflate(getContext(), R.layout.fragment_view_ticket_bottom_sheet, null);
        TextView event_namebsView = contentView.findViewById(R.id.event_namebs);
        TextView ticketIdView = contentView.findViewById(R.id.ticketId);
        event_namebsView.setText(this.event.name);
        ticketIdView.setText("# " + ticket.ticketId);
        dialog.setContentView(contentView);
        ImageView qrCodeView = contentView.findViewById(R.id.ticketQR);
        try {
            Bitmap qrBitMap = encodeAsBitmap(ticket.ticketId);
            if(qrBitMap != null) qrCodeView.setImageBitmap(qrBitMap);
        } catch (WriterException e) { }
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 200, 200, null);
        } catch (IllegalArgumentException iae) {
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 200, 0, 0, w, h);
        return bitmap;
    }
}