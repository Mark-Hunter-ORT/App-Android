package com.example.markhunters.fragments;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.example.markhunters.activities.MenuActivity;

import org.jetbrains.annotations.NotNull;

public class MarkFragment extends Fragment {
    protected String payloadKey;
    protected Context context;
    protected MenuActivity activity;


    protected void initEnvironment() {
        context = getContext();
        activity = (MenuActivity) getActivity();
        if (context == null || activity == null) {
            throw new RuntimeException("Contexto o activity nulos");
        }
    }

    protected void setPayloadKey(@NotNull final String payloadKey) {
        this.payloadKey = payloadKey;
    }

    public String getPayloadKey() {
        return payloadKey;
    }

}
