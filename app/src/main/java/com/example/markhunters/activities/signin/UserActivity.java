package com.example.markhunters.activities.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.markhunters.R;
import com.example.markhunters.activities.MenuActivity;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClient;
import com.example.markhunters.ui.LoadingDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

/**
 * This class exposes Firebase, Google and DAO services which are common between user manipulation classes. No view involved.
 */
public class UserActivity extends AppCompatActivity {
    protected FirebaseAuth fAuth;
    protected FirebaseFirestore fStore;
    protected GoogleSignInOptions gso;
    protected GoogleSignInClient gsc;
    protected LoadingDialog loadingDialog = null;
    protected static RestClient restClient = null;

    protected static void setRestClient(@NotNull RestClient restClient) {
        UserActivity.restClient = restClient;
    }

    /**
     * Just a setup
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Firebase setup
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Google setup
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        loadingDialog = new LoadingDialog(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        View mainLayoutView = getMainLayoutView();
        if (mainLayoutView != null) {
            mainLayoutView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (getCurrentFocus() != null) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    return true;
                }
            });
        }
    }

    protected View getMainLayoutView() {
        return null;
    }

    /**
     * Calls both Firebase and Google sign out service for full cache clearance
     */
    public void signout() {
        loadingDialog.start();
        fAuth.signOut(); // clear user data
        gsc.signOut().addOnCompleteListener(task -> {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            loadingDialog.dismiss();
        });
        finish();
    }

    protected void startMenuActivity(@NotNull final UserModel model) {
        final Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(MenuActivity.USER_MODEL, model);
        startActivity(intent);
    }

    public void startUserFormActivity(FirebaseUser firebaseUser) {
        final Intent intent = new Intent(this, UserFormActivity.class);
        intent.putExtra(UserFormActivity.EMAIL, firebaseUser.getEmail());
        startActivity(intent);
    }

    public class SignoutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            signout();
        }
    }
}
