package com.example.markhunters.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;

public class MainActivity extends UserActivity {

    public static final String USER_MODEL = "user_model";
    private TextView nameTextView;
    private TextView accountIdTextView;
    private UserModel model = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameTextView = findViewById(R.id.username);
        accountIdTextView =  findViewById(R.id.google_id);
        setDataOnView();

        final Button logoutBtn = findViewById(R.id.logoutBtn);
        final Button editBtn = findViewById(R.id.editButton);

        logoutBtn.setOnClickListener(new SignoutListener());
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserFormActivity(model);
            }
        });
    }

    private void setDataOnView() {
        final UserModel userModel = (UserModel) getIntent().getSerializableExtra(USER_MODEL);
        model = userModel;
        nameTextView.setText(userModel.getNickname());
        accountIdTextView.setText(userModel.getUid());
    }
}