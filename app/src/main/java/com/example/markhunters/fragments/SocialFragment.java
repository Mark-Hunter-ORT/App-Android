package com.example.markhunters.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.ui.LoadingDialog;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends MarkFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_social, container, false);
        LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.start();
        getClient().getUser("uid", new RestClientCallbacks.CallbackInstance<UserModel>() {
            @Override
            public void onSuccess(@Nullable UserModel user) {
                if (user != null) refreshUser(user);
                populateFragments(root);
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@Nullable String message) {
                System.out.println(message);
                activity.runOnUiThread(() ->  toast("Ocurrió un error actualizando los datos del usuario"));
                populateFragments(root);
                loadingDialog.dismiss();
            }
        });
        return root;
    }

    private void populateFragments(View root) {
        final UserModel user = getUser();
        TabLayout tabs = root.findViewById(R.id.social_tab);
        ViewPager viewPager = root.findViewById(R.id.tab_view_pager);
        tabs.setupWithViewPager(viewPager);

        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(new ProfileFragment(user));
        viewPagerAdapter.addFragment(new FollowingFragment(user));
        activity.runOnUiThread(() -> viewPager.setAdapter(viewPagerAdapter));
    }

    private static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        private List<TabableFragment> fragments = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(@NotNull TabableFragment fragment) {
            fragments.add(fragment);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position).getFragment();
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragments.get(position).getTitle();
        }
    }
}