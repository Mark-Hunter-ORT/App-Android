package com.example.markhunters.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.markhunters.R;
import com.example.markhunters.fragments.MapFragment;
import com.example.markhunters.fragments.MarkFragment;
import com.example.markhunters.fragments.ProfileFragment;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.ServiceProvider;
import com.example.markhunters.service.rest.RestClient;
import com.example.markhunters.activities.signin.UserActivity;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MenuActivity extends UserActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String USER_MODEL = "user_model";
    private DrawerLayout drawer;
    private UserModel userOld;
    private static String token = null;
    private static UserModel user = null;
    private RestClient restClient;

    public static void setToken(String token) {
        MenuActivity.token = token;
    }
    public static void setUser(UserModel user) { MenuActivity.user = user; };

    public RestClient getClient() {
        return this.restClient;
    }
    public UserModel getUser() { return user; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        validateToken();
        restClient = ServiceProvider.getRestClient(token);

        setDataOnView();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView mName = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        if (userOld.getPhotoStringUri() != null && userOld.getDisplayName() != null) {
            ImageView mPic = navigationView.getHeaderView(0).findViewById(R.id.imageView);
            mName.setText(userOld.getDisplayName());
            mPic.setImageURI(userOld.getPhotoUri());
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            navigate(R.id.menu_map);
            navigationView.setCheckedItem(R.id.menu_map);
        }
    }

    private void validateToken() {
        if (token == null || TextUtils.isEmpty(token)) {
            throw new RuntimeException("Token inválido");
        }
    }

    @Override
    protected View getMainLayoutView() {
        return findViewById(R.id.drawer_layout);
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
        navigate(item.getItemId());
        return true;
    }

    private void navigate(int itemId) {
        switch (itemId) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment(userOld)).commit();
                break;
            case R.id.menu_map:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                break;
            case R.id.signout:
                signout();
        }
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setDataOnView() {
        userOld = (UserModel) getIntent().getSerializableExtra(USER_MODEL);
    }

    public void goToFragment(@NotNull MarkFragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public String getUserUid() {
        return userOld.getId();
    }
}