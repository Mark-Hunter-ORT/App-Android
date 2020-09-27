package com.example.markhunters.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.markhunters.R;

import org.jetbrains.annotations.NotNull;

/**
 * How does this work?
 * Define an instance of LoadingDialog in your activity, passing the activity instance in the constructor (this).
 * Call start() method to show the spinner_dialog view. It uses the activity's inflater to get on the front of the view.
 * Call dismiss() method to close the spinner_dialog.
 */
public class LoadingDialog {
    private AlertDialog dialog;

    public LoadingDialog(@NotNull final Activity activity) {
        this(activity, "Loading"); // default message
    }

    public LoadingDialog(@NotNull final Activity activity, @NotNull final String loadingMessage) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final LayoutInflater inflater = activity.getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.spinner_dialog, null);
        TextView loadingTextView = inflatedView.findViewById(R.id.loadingTextView);
        loadingTextView.setText(loadingMessage);
        builder.setView(inflatedView);
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
