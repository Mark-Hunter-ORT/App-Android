package com.example.markhunters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.activities.signin.UserActivity;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends MarkFragment
{
    private UserModel user;
    private TextView nicknameTextView;
    private TextView emailTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        nicknameTextView = rootView.findViewById(R.id.nicknameTextView);
        emailTextView = rootView.findViewById(R.id.emailTextView);

        if (user != null) {
            nicknameTextView.setText(user.getNickname());
            emailTextView.setText(user.getEmail());
        }

        Button editButton = rootView.findViewById(R.id.editButton);
        editButton.setOnClickListener(view -> {
            activity.startUserFormActivity(user);
        });

        return rootView;
    }
    public ProfileFragment(@NotNull final UserModel user) {
        this.user = user;
    }
}
