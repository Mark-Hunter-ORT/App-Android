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
import com.example.markhunters.model.UserFollowing;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.ui.LoadingDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FollowingFragment extends MarkFragment implements TabableFragment {

    private boolean fetched;
    private TableLayout table;
    private List<UserFollowing> userFollowings; // en memoria

    public FollowingFragment() {
        userFollowings = new ArrayList<>();
        fetched = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_following, container, false);
        table = root.findViewById(R.id.followingTable);
        return root;
    }

    private void populateTable() {
        activity.runOnUiThread(() -> {
            table.removeAllViews();
            if (userFollowings.isEmpty()) {
                TableRow tableRow = new TableRow(context);
                TextView cell = buildCell("No estás siguiendo a ningún MarkHu");
                tableRow.addView(cell);
                table.addView(tableRow);
            } else {
                userFollowings.forEach(uf -> {
                    TableRow tableRow = new TableRow(context);
                    TextView nameCell = buildCell(uf.getUsername());
                    tableRow.addView(nameCell);
                    Button unfollowBtn = buildUnfollowBtn(uf.getUid(), uf.getUsername());
                    tableRow.addView(unfollowBtn);
                    table.addView(tableRow);
                });
            }
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
                                activity.runOnUiThread(() -> {
                                    userFollowings = userFollowings.stream().filter(uf -> !uf.getUid().equals(id)).collect(Collectors.toList());
                                    activity.runOnUiThread(() -> toast("Has dejado de seguir a " + name));
                                    populateTable();
                                });
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

    @Override
    public void refresh() {
        if (!fetched) {
            LoadingDialog loadingDialog = new LoadingDialog(activity);
            loadingDialog.start();
            getClient().getFollowings(new RestClientCallbacks.CallbackCollection<UserFollowing>() {
                @Override
                public void onFailure(@Nullable String message) {
                    System.out.println(message);
                    activity.runOnUiThread(() -> toast("Ocurrió un error obteniendo los usuarios seguidos"));
                    loadingDialog.dismiss();
                }

                @Override
                public void onSuccess(List<UserFollowing> followings) {
                    activity.runOnUiThread(() -> {
                        userFollowings = followings;
                        loadingDialog.dismiss();
                        populateTable();
                    });
                }
            });
            fetched = true;
        }
    }
}