package com.example.markhunters.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.markhunters.R;
import com.example.markhunters.fragments.MapFragment;
import com.example.markhunters.fragments.MarkFragment;
import com.example.markhunters.fragments.MarkCreationFragment;
import com.example.markhunters.fragments.ProfileFragment;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.signin.UserActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class MenuActivity extends UserActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String USER_MODEL = "user_model";
    private DrawerLayout drawer;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // hide input keyboard when clicking outside
        findViewById(R.id.drawer_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        setDataOnView();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TextView mName = navigationView.getHeaderView(0).findViewById(R.id.textViewName);
        if (user.getPhotoStringUri() != null && user.getDisplayName() != null) {
            ImageView mPic = navigationView.getHeaderView(0).findViewById(R.id.imageView);
            mName.setText(user.getDisplayName());
            mPic.setImageURI(user.getPhotoUri());
        }


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            navigate(R.id.menu_map);
            navigationView.setCheckedItem(R.id.menu_map);
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
        navigate(item.getItemId());
        return true;
    }

    private void navigate(int itemId) {
        switch (itemId) {
            case R.id.profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment(user)).commit();
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
        user = (UserModel) getIntent().getSerializableExtra(USER_MODEL);
    }

    public void goToFragment(MarkFragment fragment, @Nullable Serializable payload) {
        if (payload != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(fragment.getPayloadKey(), payload);
            fragment.setArguments(bundle);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public void goToFragment(MarkFragment fragment) {
        goToFragment(fragment, null);
    }

    public String getUserUid() {
        return user.getUid();
    }
}