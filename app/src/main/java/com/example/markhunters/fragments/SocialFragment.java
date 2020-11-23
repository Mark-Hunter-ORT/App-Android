package com.example.markhunters.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.markhunters.R;
import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SocialFragment extends MarkFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_social, container, false);
        populateFragments(root);
        return root;
    }

    private void populateFragments(View root) {
        TabLayout tabs = root.findViewById(R.id.social_tab);
        ViewPager viewPager = root.findViewById(R.id.tab_view_pager);
        tabs.setupWithViewPager(viewPager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(activity.getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(new ProfileFragment());
        viewPagerAdapter.addFragment(new FollowingFragment());
        activity.runOnUiThread(() -> {
            viewPager.setAdapter(viewPagerAdapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    TabableFragment item = viewPagerAdapter.getItemTabable(position);
                    item.refresh();
                }

                @Override
                public void onPageSelected(int position) {
                    TabableFragment item = viewPagerAdapter.getItemTabable(position);
                    item.refresh();
                }

                @Override
                public void onPageScrollStateChanged(int state) {}
            });

         });

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

        @NonNull
        public TabableFragment getItemTabable(int position) {
            return fragments.get(position);
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