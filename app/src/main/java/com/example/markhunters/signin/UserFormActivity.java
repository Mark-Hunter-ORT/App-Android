package com.example.markhunters.signin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.markhunters.R;
import com.example.markhunters.dao.DaoCallback;
import com.example.markhunters.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserFormActivity extends UserActivity {

    public static final String USER_MODEL = "firebase_user";
    private TextView nicknamePlainText;
    private TextView emailTextView;
    private String uid;
    private UserModel originalModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);

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
        final Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    // build the model that will be inserted in the database
                    final UserModel toPersist = new UserModel(uid, nicknamePlainText.getText().toString(), emailTextView.getText().toString());

                    // persist method results in a Firebase task that is passed by callback
                    dao.persist(toPersist, new DaoCallback<UserModel>() {
                        @Override
                        public void onTaskCallback(Task<Void> task) {
                            task.addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // When this point is reached it means Firebase finished persisting the new user
                                    Toast.makeText(UserFormActivity.this, "User saved.", Toast.LENGTH_SHORT).show();
                                    // To avoid inconsistencies, discard toPersist model and retrieve the user that was just created
                                    dao.find(uid, new DaoCallback<UserModel>() {
                                        @Override
                                        public void onActionCallback(UserModel model) {
                                            if (model != null) {
                                                startMainActivity(model);
                                            } // Todo else ERROR
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });

        final Button cancelButton = findViewById(R.id.cancelButton);
        /**
         * originalModel is an existing one that is being edited.
         * If cancel button is invoked signout must be avoided.
         * Instead start main activity again.
         */
        if (originalModel != null) {
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startMainActivity(originalModel);
                }
            });
        } else {
            cancelButton.setOnClickListener(new SignoutListener());
        }
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
        final UserModel userModel = (UserModel) getIntent().getSerializableExtra(USER_MODEL);
        emailTextView.setText(userModel.getEmail());
        uid = userModel.getUid();
        if (userModel.getNickname() != null) { // Existing user, "Edit" was invoked
            nicknamePlainText.setText(userModel.getNickname());
            originalModel = userModel;
        }
    }
}