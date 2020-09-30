package com.example.markhunters.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.markhunters.R;
import com.example.markhunters.fragments.MapFragment;
import com.example.markhunters.fragments.PictureTestFragment;
import com.example.markhunters.fragments.ProfileFragment;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.signin.UserActivity;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends UserActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String USER_MODEL = "user_model";
    private DrawerLayout drawer;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setDataOnView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment(user)).commit();
            navigationView.setCheckedItem(R.id.profile);
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment(user)).commit();
                break;
            case R.id.menu_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.takePictureTest:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PictureTestFragment()).commit();
                break;
            case R.id.exit:
                signout();
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setDataOnView() {
        user = (UserModel) getIntent().getSerializableExtra(USER_MODEL);
    }
}