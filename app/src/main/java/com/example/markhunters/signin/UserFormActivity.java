package com.example.markhunters.signin;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.markhunters.R;
import com.example.markhunters.dao.DaoCallback;
import com.example.markhunters.model.UserModel;

public class UserFormActivity extends UserActivity {

    public static final String USER_MODEL = "firebase_user";
    private TextView nicknamePlainText;
    private TextView emailTextView;
    private String uid;
    private UserModel originalModel = null;
    private String displayName;
    private String photoStringUri;

    @Override
    protected View getMainLayoutView() {
        return this.findViewById(R.id.constraintLayout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_form);

        // View data setup
        nicknamePlainText = findViewById(R.id.nicknamePlainText);
        emailTextView =  findViewById(R.id.emailTextView);
        setDataOnView();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Creation
        final Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    loadingDialog.start();
                    // build the model that will be inserted in the database
                    final UserModel toPersist = new UserModel(uid, nicknamePlainText.getText().toString(), emailTextView.getText().toString(), displayName, photoStringUri);
                    dao.persist(toPersist, new DaoCallback<UserModel>() {
                        @Override
                        public void onCallback(UserModel model) {
                            if (model != null) {
                                Toast.makeText(UserFormActivity.this, "Usuario guardado", Toast.LENGTH_SHORT).show();
                                startMenuActivity(model);
                                loadingDialog.dismiss();
                            } // Todo else ERROR
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
                    startMenuActivity(originalModel);
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
        photoStringUri = userModel.getPhotoStringUri();
        displayName = userModel.getDisplayName();
        if (userModel.getNickname() != null) { // Existing user, "Edit" was invoked
            nicknamePlainText.setText(userModel.getNickname());
            originalModel = userModel;
        }
    }
}