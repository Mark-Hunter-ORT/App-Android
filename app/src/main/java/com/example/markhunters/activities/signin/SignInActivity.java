package com.example.markhunters.activities.signin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.ServiceProvider;
import com.example.markhunters.service.rest.RestClient;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class SignInActivity extends UserActivity {
    private static final String TAG = "AndroidClarified";
    private static final int RC_SIGN_IN = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth.signOut(); // clear user data
        gsc.signOut();
        setContentView(R.layout.activity_sign_in);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        final SignInButton gsButton = findViewById(R.id.sign_in_button);
        gsButton.setOnClickListener(v -> {
            // Here we should check internet connection
            final Intent signInIntent = gsc.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
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

    private void onLoggedIn(@NotNull final FirebaseUser firebaseUser) {
        firebaseUser.getIdToken(true).addOnCompleteListener(task -> {
            GetTokenResult result = task.getResult();
            String token = result.getToken();
            RestClient restClient = ServiceProvider.getRestClient(token);
            UserActivity.setRestClient(restClient);
            UserActivity.restClient.getMyUser(new RestClientCallbacks.CallbackInstance<UserModel>() {
                @Override
                public void onFailure(@Nullable String message) {
                    System.out.println(message);
                    runOnUiThread(() -> Toast.makeText(SignInActivity.this, "Ocurrió un problema obteniendo el usuario", Toast.LENGTH_SHORT).show());
                    loadingDialog.dismiss();
                }
                @Override
                public void onSuccess(@Nullable UserModel userModel) {
                    if (userModel == null) {
                        startUserFormActivity(firebaseUser); // creation
                    } else {
                        userModel.setFirebaseData(firebaseUser);
                        startMenuActivity(userModel); // user exists, go to main activity
                    }
                    loadingDialog.dismiss();
                    finish();
                }

                @Override
                public void onError(@Nullable String message, int code) {
                    if (code == 404) {
                        onSuccess(null);
                    } else {
                        onFailure(message);
                    }
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // check if a user is already logged
        final FirebaseUser currentUser = fAuth.getCurrentUser();
        if (currentUser != null) {
            onLoggedIn(currentUser);
        }
    }

    /**
     * When a user signs in. It was not present in the session.
     * @param idToken idToken
     */
    private void firebaseAuthWithGoogle(String idToken) {
        loadingDialog.start();
        final AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = fAuth.getCurrentUser();
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
