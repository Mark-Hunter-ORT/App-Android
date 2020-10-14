package com.example.markhunters.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.markhunters.R;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.signin.UserActivity;
import com.example.markhunters.signin.UserFormActivity;
import com.example.markhunters.ui.LoadingDialog;

import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class PictureTestFragment extends Fragment
{
    private ImageView mImageView;
    private Button uploadButton;
    private Bitmap mBitmap; // todo this is what has to be stored in Firebase
    private static final int CAMERA_REQUEST_CODE = 1001;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_picture_test, container, false);

        uploadButton = rootView.findViewById(R.id.uploadPictureButton);
        uploadButton.setEnabled(false); // no picture to upload yet
        mImageView = rootView.findViewById(R.id.cameraView);

        // 'take a picture' button action
        rootView.findViewById(R.id.takePictureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        // 'upload' button action.
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: This is just a placeholder. Here we should call a DAO and store mBitmap in Firebase.
                LoadingDialog loadingDialog = new LoadingDialog(getActivity(), "Subiendo");
                loadingDialog.start();
                (new Handler()).postDelayed(() -> {
                    loadingDialog.dismiss();
                    Toast.makeText(getContext(), "Foto subida!", Toast.LENGTH_SHORT).show();
                }, 5000); // dismiss the dialog after 5 seconds. Show success message toast.
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                mImageView.setImageBitmap(bitmap);
                mBitmap = bitmap; // todo this is what has to be stored in Firebase
                uploadButton.setEnabled(mImageView != null);
            }
        }
    }
}
