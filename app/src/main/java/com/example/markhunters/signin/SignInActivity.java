package com.example.markhunters.signin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.example.markhunters.R;
import com.example.markhunters.dao.UserDao;
import com.example.markhunters.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.Serializable;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "AndroidClarified";
    private GoogleSignInClient gsc;
    private SignInButton gsButton;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // request the user's ID, email address, and basic. DEFAULT_SIGN_IN includes ID and basic profile info
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // build a client with the options specified by gso
        gsc = GoogleSignIn.getClient(this, gso);

        // find sign in button view
        gsButton = findViewById(R.id.sign_in_button);
        gsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Here we should check internet connection
                final Intent signInIntent = gsc.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        final Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        final GoogleSignInAccount account = task.getResult(ApiException.class);
                        onLoggedIn(account);
                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }

    }

    // this is the last step, creates a MarkHunterUser model from google account and navigates to the app's main activity
    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {

        final Intent intent = new Intent(this, MainActivity.class);
        final UserModel userModel = UserDao.getInstance().find(googleSignInAccount.getId());
        if (userModel == null) // todo prompt creation (nickname only)
        intent.putExtra(MainActivity.USER_MODEL, (Serializable) userModel);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if a user is already logged
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            onLoggedIn(account);
        }
    }
}
