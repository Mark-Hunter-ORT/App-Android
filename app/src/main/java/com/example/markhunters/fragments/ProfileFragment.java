package com.example.markhunters.fragments;

import android.annotation.SuppressLint;
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

public class ProfileFragment extends MarkFragment implements TabableFragment
{
    private UserModel user;

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        TextView nicknameTextView = rootView.findViewById(R.id.nicknameTextView);
        TextView emailTextView = rootView.findViewById(R.id.emailTextView);
        TextView followersTextView = rootView.findViewById(R.id.followersTextView);

        // todo tengo que hacer un fetch para los followers

        if (user != null) {
            nicknameTextView.setText(user.getNickname());
            emailTextView.setText(user.getEmail());
            followersTextView.setText("Tienes " + user.getFollowers() + " seguidores!");
        }
        return rootView;
    }
    public ProfileFragment(@NotNull final UserModel user) {
        this.user = user;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public String getTitle() {
        return "Perfil";
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
