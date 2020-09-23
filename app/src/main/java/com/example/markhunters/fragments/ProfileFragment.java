package com.example.markhunters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;

import org.jetbrains.annotations.NotNull;

public class ProfileFragment extends Fragment
{
    private UserModel user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nicknameTextView = root.findViewById(R.id.nicknameTextView);
        if (user != null)
            nicknameTextView.setText(user.getNickname());
        return root;
    }
    public ProfileFragment(@NotNull final UserModel user) {
        this.user = user;
    }
}
