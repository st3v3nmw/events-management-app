package com.aspinax.lanaevents;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class AlertDialog {
    AlertDialog() { }

    public void showDialog(Activity activity, String title, boolean error){
        final android.app.Dialog dialog = new android.app.Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView titleView = dialog.findViewById(R.id.title);
        titleView.setText(title);

        if (error) {
            Button done = dialog.findViewById(R.id.Done);
            done.setBackgroundColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.red));
        }

        Button dialogButton = dialog.findViewById(R.id.Done);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}