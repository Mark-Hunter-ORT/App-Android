package com.example.markhunters.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.ui.LoadingDialog;

import java.util.Map;

public class FollowingFragment extends MarkFragment implements TabableFragment {

    private final UserModel user;
    private TableLayout table;

    public FollowingFragment(UserModel user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_following, container, false);
        table = root.findViewById(R.id.followingTable);
        populateTable();
        return root;
    }

    private void populateTable() {
        table.removeAllViews();
        final Map<String, String> following = user.getFollowing();
        following.forEach((id, name) -> {
            TableRow tableRow = new TableRow(context);
            TextView nameCell = buildCell(name);
            tableRow.addView(nameCell);
            Button unfollowBtn = buildUnfollowBtn(id, name);
            tableRow.addView(unfollowBtn);
            table.addView(tableRow);
        });
    }

    private Button buildUnfollowBtn(String id, String name) {
        final Button unfollowBtn = new Button(context, null, R.style.Widget_AppCompat_Button_Borderless);
        unfollowBtn.setText(R.string.eliminar);
        unfollowBtn.setOnClickListener(view -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle("Dejar de seguir")
                    .setMessage(String.format("¿Seguro desea dejar de seguir a %s?", name))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        LoadingDialog loadingDialog = new LoadingDialog(activity, "Eliminando");
                        loadingDialog.start();
                        getClient().unfollowUser(id, new RestClientCallbacks.CallbackAction() {
                            @Override
                            public void onSuccess() {
                                user.removeFollowing(id);
                                activity.runOnUiThread(() -> populateTable());
                                loadingDialog.dismiss();
                            }

                            @Override
                            public void onFailure(@Nullable String message) {
                                System.out.println(message);
                                activity.runOnUiThread(() -> toast("Ocurrió un error intentando realizar la operación"));
                                loadingDialog.dismiss();
                            }
                        });
                    }).setNegativeButton(android.R.string.no, null).create();
            alertDialog.setOnShowListener(arg0 -> {
                Button cancelBtn = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                cancelBtn.setText(R.string.cancel);
                cancelBtn.setTextColor(Color.BLACK);
                Button okBtn = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                okBtn.setTextColor(Color.BLACK);
            });
            alertDialog.show();
        });
        return unfollowBtn;
    }

    private TextView buildCell(Object data) {
        TextView cell = new TextView(context);
        cell.setText(String.valueOf(data));
        return style(cell);
    }

    private TextView style(TextView cell) {
        cell.setPadding(3, 3, 3, 3);
        cell.setTextSize(20);
        cell.setTextColor(Color.BLACK);
        cell.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.7f));
        return cell;
    }

    @Override
    public String getTitle() {
        return "Siguiendo";
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}