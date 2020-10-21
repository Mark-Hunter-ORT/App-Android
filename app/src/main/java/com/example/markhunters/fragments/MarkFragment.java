package com.example.markhunters.fragments;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.markhunters.activities.MenuActivity;

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

    protected void goToFragment(MarkFragment fragment) {
        activity.goToFragment(fragment);
    }

}
