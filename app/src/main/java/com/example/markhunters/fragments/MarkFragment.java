package com.example.markhunters.fragments;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.markhunters.activities.MenuActivity;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClient;

import org.jetbrains.annotations.NotNull;

public class MarkFragment extends Fragment {
    protected Context context;
    protected MenuActivity activity;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initEnvironment();
    }

    protected void initEnvironment() {
        context = getContext();
        activity = (MenuActivity) getActivity();
        if (context == null || activity == null) {
            throw new RuntimeException("Contexto o activity nulos");
        }
    }

    protected RestClient getClient() {
        return activity.getClient();
    }

    protected void goToFragment(MarkFragment fragment) {
        activity.goToFragment(fragment);
    }

    protected void toast(@NotNull final String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    protected UserModel getUser() {
        return activity.getUser();
    }

    protected void refreshUser(@NotNull UserModel user) {
        activity.setUser(user);
    }
}
