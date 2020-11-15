package com.example.markhunters.activities.signin;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.ServiceProvider;
import com.example.markhunters.service.rest.RestClient;
import com.example.markhunters.service.rest.RestClientCallbacks;

public class UserFormActivity extends UserActivity {

    public static final String USER_MODEL = "firebase_user";
    private TextView nicknamePlainText;
    private TextView emailTextView;
    private String uid;
    private UserModel originalModel = null;
    private String displayName;
    private String photoStringUri;
    private RestClient restClient;
    private String token;


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
        restClient = ServiceProvider.getRestClient(token);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Creation
        final Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            if (validateFields()) {
                loadingDialog.start();
                // build the model that will be inserted in the database
                restClient.postUser(nicknamePlainText.getText().toString(), new RestClientCallbacks.CallbackInstance<UserModel>() {
                    @Override
                    public void onSuccess(@Nullable UserModel model) {
                        if (model != null) {
                            model.setToken(token);
                            runOnUiThread(() -> Toast.makeText(UserFormActivity.this, "Usuario guardado", Toast.LENGTH_SHORT).show());
                            startMenuActivity(model);
                            loadingDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(@Nullable String message) {
                        System.out.println(message);
                        Toast.makeText(UserFormActivity.this, "Ocurrió un problema guardando el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        final Button cancelButton = findViewById(R.id.cancelButton);
        /**
         * originalModel is an existing one that is being edited.
         * If cancel button is invoked signout must be avoided.
         * Instead start main activity again.
         */
        if (originalModel != null) {
            cancelButton.setOnClickListener(view -> startMenuActivity(originalModel));
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
        token = userModel.getToken();
        if (userModel.getNickname() != null) { // Existing user, "Edit" was invoked
            nicknamePlainText.setText(userModel.getNickname());
            originalModel = userModel;
        }
    }
}