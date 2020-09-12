package com.example.markhunters.signin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;

public class UserCreationActivity extends AppCompatActivity {

    public static final String FIREBASE_USER = "firebase_user";
    private TextView nicknameTextView;
    private TextView emailTextView;
    private String uid;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private UserModel model = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        nicknameTextView = findViewById(R.id.nicknameTextView);
        emailTextView =  findViewById(R.id.emailTextView);

        final Button createButton = findViewById(R.id.createButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    Toast.makeText(UserCreationActivity.this, "User created.", Toast.LENGTH_SHORT).show();
                    model = new UserModel(uid, nicknameTextView.getText().toString(), emailTextView.getText().toString());
                    final DocumentReference userReference = fStore.collection("users").document(uid);
                    userReference.set(model.buildDTO()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            final Intent intent = new Intent(UserCreationActivity.this, MainActivity.class);
                            intent.putExtra(MainActivity.USER_MODEL, (Serializable) model);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        final Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.signOut(); // clear user data
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        setDataOnView();
    }

    private boolean validateFields() {
        boolean allFieldsValid = true;
        if (TextUtils.isEmpty(nicknameTextView.getText().toString())) {
            allFieldsValid = false;
            nicknameTextView.setError("Required field");
        }
        return allFieldsValid;
    }


    private void setDataOnView() {
        final FirebaseUser firebaseUser = getIntent().getParcelableExtra(FIREBASE_USER);
        emailTextView.setText(firebaseUser.getEmail());
        uid = firebaseUser.getUid();
    }
}