package com.example.markhunters.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.markhunters.R;
import com.example.markhunters.model.Content;
import com.example.markhunters.model.Mark;
import com.example.markhunters.model.MarkLocation;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.ui.LoadingDialog;

import java.util.ArrayList;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.markhunters.service.base64.Base64Utils.toBase64;

public class MarkCreationFragment extends MarkFragment
{
    private ImageView mImageView;
    private EditText markTagText;
    private Bitmap mBitmap = null;
    private MarkLocation markLocation;
    private static final int CAMERA_REQUEST_CODE = 1001;

    public MarkCreationFragment(MarkLocation markLocation) {
        this.markLocation = markLocation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_mark_creation, container, false);
        Button uploadButton = rootView.findViewById(R.id.saveMarkButton);
        mImageView = rootView.findViewById(R.id.cameraView);
        markTagText = rootView.findViewById(R.id.markTagEditText);

        // 'take a picture' button action
        rootView.findViewById(R.id.takePictureBtn).setOnClickListener(view -> {
            Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        });

        rootView.findViewById(R.id.cancelMarkButton).setOnClickListener(view -> goToFragment(new MapFragment()));

        // 'upload' button action.
        uploadButton.setOnClickListener(view -> {
            if (validateFields()) {
                LoadingDialog loadingDialog = new LoadingDialog(activity, "Subiendo");
                loadingDialog.start();
                final Mark mark = buildMark();
                getClient().postMark(mark, new RestClientCallbacks.CallbackAction() {
                    @Override
                    public void onFailure(@Nullable String message) {
                        activity.runOnUiThread(() -> Toast.makeText(context, "Ocurrió un error guardando el Mark", Toast.LENGTH_SHORT).show());
                        loadingDialog.dismiss();
                    }
                    @Override
                    public void onSuccess() {
                        goToFragment(new MapFragment());
                        loadingDialog.dismiss();
                    }
                });
            }
        });

        return rootView;
    }

    private boolean validateFields() {
        if (mBitmap == null) {
            Toast.makeText(getContext(), "Debe tomar una foto!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(markTagText.getText())) {
            Toast.makeText(getContext(), "Debe escribir un título!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private Mark buildMark() {
        ArrayList<String> contentImageList = new ArrayList<>();
        String base64bitmap = toBase64(mBitmap);
        contentImageList.add(base64bitmap);
        String text = markTagText.getText().toString();
        Content content = new Content(text, contentImageList);
        return new Mark(getUserId(), "test_cat", markLocation, content);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                final Bitmap bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                mImageView.setImageBitmap(bitmap);
                mBitmap = bitmap;
            }
        }
    }
}
