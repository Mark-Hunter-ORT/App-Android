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
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.ui.LoadingDialog;

public class ProfileFragment extends MarkFragment implements TabableFragment
{
    private UserModel user = null;
    private boolean fetched;
    private TextView nicknameTextView;
    private TextView emailTextView;
    private TextView followersTextView;

    public ProfileFragment() {
        fetched = false;
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        nicknameTextView = rootView.findViewById(R.id.nicknameTextView);
        emailTextView = rootView.findViewById(R.id.emailTextView);
        followersTextView = rootView.findViewById(R.id.followersTextView);
        refresh();

        return rootView;
    }

    private void populateView() {
        if (user != null) {
            nicknameTextView.setText(user.getNickname());
            emailTextView.setText(user.getEmail());
            followersTextView.setText(resolveFollowersText(user.getFollowers()));
        }
    }

    private String resolveFollowersText(int followers) {
        if (followers == 0) {
            return "Aún no tienes seguidores";
        } else if (followers == 1) {
            return "Tienes 1 seguidor!";
        } else {
            return "Tienes " + followers + " seguidores!";
        }
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

    @Override
    public void refresh() {
        if (!fetched) {
            LoadingDialog loadingDialog = new LoadingDialog(activity);
            loadingDialog.start();
            getClient().getMyUser(new RestClientCallbacks.CallbackInstance<UserModel>() {
                @Override
                public void onSuccess(@Nullable UserModel user) {
                    if (user != null) refreshUser(user);
                    ProfileFragment.this.user = user;
                    loadingDialog.dismiss();
                    activity.runOnUiThread(() -> populateView());
                }

                @Override
                public void onFailure(@Nullable String message) {
                    System.out.println(message);
                    activity.runOnUiThread(() ->  toast("Ocurrió un error actualizando los datos del usuario"));
                    loadingDialog.dismiss();
                }
            });
            fetched = true;
        }
    }
}
