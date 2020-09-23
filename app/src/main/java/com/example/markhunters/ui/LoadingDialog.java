package com.example.markhunters.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.markhunters.R;

import org.jetbrains.annotations.NotNull;

/**
 * How does this work?
 * Define an instance of LoadingDialog in your activity, passing the activity instance in the constructor (this).
 * Call start() method to show the spinner_dialog view. It uses the activity's inflater to get on the front of the view.
 * Call dismiss() method to close the spinner_dialog.
 */
public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(@NotNull final Activity activity) {
        this.activity = activity;
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.spinner_dialog, null));
        builder.setCancelable(false);
        dialog = builder.create();
    }

    public void start () {
        dialog.show();
    }

    public void dismiss () {
        dialog.dismiss();
    }
}
