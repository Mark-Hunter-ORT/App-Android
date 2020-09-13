package com.example.markhunters.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.markhunters.R;
import com.example.markhunters.dao.Dao;
import com.example.markhunters.dao.DaoProvider;
import com.example.markhunters.model.UserModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class UserCreationActivity extends AppCompatActivity {

    public static final String FIREBASE_USER = "firebase_user";
    private TextView nicknamePlainText;
    private TextView emailTextView;
    private String uid;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private UserModel model = null;
    private Dao<UserModel> dao;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

        // Firebase setup
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        // Initialize dao
        dao = DaoProvider.getUserDao();

        // View data setup
        nicknamePlainText = findViewById(R.id.nicknamePlainText);
        emailTextView =  findViewById(R.id.emailTextView);
        setDataOnView();

        // hide input keyboard when clicking outside
        findViewById(R.id.constraintLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                return true;
            }
        });

        // Creation
        final Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Toast.makeText(UserCreationActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                    Task<Void> created = dao.persist(new UserModel(uid, nicknamePlainText.getText().toString(), emailTextView.getText().toString()));
                    created.addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            model = dao.find(uid); // retrieve the user that was just created
                            if (model != null) {
                                startMainActivity(model);
                            }
                            // Todo else ERROR
                        }
                    });
                }
            }
        });

        // Cancel, go back to signin
        final Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut(); // clear user data
                gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    private void startMainActivity(@NotNull final UserModel model) {
        final Intent intent = new Intent(UserCreationActivity.this, MainActivity.class);
        intent.putExtra(MainActivity.USER_MODEL, model);
        startActivity(intent);
    }

    private boolean validateFields() {
        boolean allFieldsValid = true;
        if (TextUtils.isEmpty(nicknamePlainText.getText().toString())) {
            allFieldsValid = false;
            nicknamePlainText.setError("Required field");
        }
        return allFieldsValid;
    }


    private void setDataOnView() {
        final FirebaseUser firebaseUser = getIntent().getParcelableExtra(FIREBASE_USER);
        emailTextView.setText(firebaseUser.getEmail());
        uid = firebaseUser.getUid();
    }
}