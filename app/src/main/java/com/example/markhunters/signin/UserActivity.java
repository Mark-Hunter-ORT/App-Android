package com.example.markhunters.signin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.markhunters.MainActivity;
import com.example.markhunters.R;
import com.example.markhunters.dao.Dao;
import com.example.markhunters.dao.DaoProvider;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.ui.LoadingDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

/**
 * This class exposes Firebase, Google and DAO services which are common between user manipulation classes. No view involved.
 */
public class UserActivity extends AppCompatActivity {
    protected FirebaseAuth fAuth;
    protected FirebaseFirestore fStore;
    protected Dao<UserModel> dao;
    protected GoogleSignInOptions gso;
    protected GoogleSignInClient gsc;
    protected LoadingDialog loadingDialog = null;

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

        // Dao setup
        dao = DaoProvider.getUserDao();

        loadingDialog = new LoadingDialog(this);
    }

    /**
     * Calls both Firebase and Google sign out service for full cache clearance
     */
    private void signout() {
        loadingDialog.start();
        fAuth.signOut(); // clear user data
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                loadingDialog.dismiss();
            }
        });
    }

    protected void startMainActivity(@NotNull final UserModel model) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USER_MODEL, model);
        startActivity(intent);
    }

    protected void startUserFormActivity(@NotNull final UserModel model) {
        final Intent intent = new Intent(this, UserFormActivity.class);
        intent.putExtra(UserFormActivity.USER_MODEL, model);
        startActivity(intent);
    }

    public class SignoutListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            signout();
        }
    }
}
