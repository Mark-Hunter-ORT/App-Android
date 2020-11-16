package com.example.markhunters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends MarkFragment
{
    private UserModel user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nicknameTextView = rootView.findViewById(R.id.nicknameTextView);
        TextView emailTextView = rootView.findViewById(R.id.emailTextView);

        if (user != null) {
            nicknameTextView.setText(user.getNickname());
            emailTextView.setText(user.getEmail());
        }
        return rootView;
    }
    public ProfileFragment(@NotNull final UserModel user) {
        this.user = user;
    }
}
