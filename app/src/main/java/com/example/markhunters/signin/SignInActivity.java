package com.example.markhunters.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.markhunters.R;
import com.example.markhunters.dao.DaoProvider;
import com.example.markhunters.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "AndroidClarified";
    private static final int RC_SIGN_IN = 101;
    private GoogleSignInClient gsc;
    private SignInButton gsButton;
    private GoogleSignInOptions gso;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        // request the user's ID, email address, and basic. DEFAULT_SIGN_IN includes ID and basic profile info
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
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
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_SIGN_IN) {
                final Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    final GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // The ApiException status code indicates the detailed failure reason.
                    Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                }
            }
        }
    }

    /**
     * Last step. FirebaseUser is already authenticated. Navigate to MainActivity with user from database or go to UserCreationActivity to register it
     * @param firebaseUser
     */
    private void onLoggedIn(@NotNull final FirebaseUser firebaseUser) {
        final String uid = firebaseUser.getUid();
        final UserModel userModel = DaoProvider.getUserDao().find(uid);
        if (userModel == null)
            startUserCreationActivity(firebaseUser);
        else startMainActivity(userModel);
        finish();
    }

    private void startUserCreationActivity(@NotNull final FirebaseUser firebaseUser) {
        final Intent intent = new Intent(this, UserCreationActivity.class);
        intent.putExtra(UserCreationActivity.FIREBASE_USER, (Parcelable) firebaseUser);
        startActivity(intent);
    }

    private void startMainActivity(@NotNull final UserModel userModel) {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.USER_MODEL, (Serializable) userModel);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if a user is already logged
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            onLoggedIn(currentUser);
        }
    }

    /**
     * When a user signs in. It was not present in the session.
     * @param idToken
     */
    private void firebaseAuthWithGoogle(String idToken) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            onLoggedIn(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
    }
}
