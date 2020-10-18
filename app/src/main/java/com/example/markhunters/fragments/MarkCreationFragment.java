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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.markhunters.R;
import com.example.markhunters.model.Marca;
import com.example.markhunters.ui.LoadingDialog;

import static android.app.Activity.RESULT_OK;

public class MarkCreationFragment extends MarkFragment
{
    private ImageView mImageView;
    private EditText markTagText;
    private Button uploadButton;
    private Bitmap mBitmap = null; // todo this is what has to be stored in Firebase
    private Marca mark;
    private static final int CAMERA_REQUEST_CODE = 1001;
    private final String PAYLOAD_KEY = "Mark";

    public MarkCreationFragment() {
        super();
        setPayloadKey(PAYLOAD_KEY);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initEnvironment();
        View rootView = inflater.inflate(R.layout.fragment_mark_creation, container, false);
        uploadButton = rootView.findViewById(R.id.saveMarkButton);
        mImageView = rootView.findViewById(R.id.cameraView);
        markTagText = rootView.findViewById(R.id.markTagEditText);
        if (getArguments() != null) {
            mark = (Marca) getArguments().getSerializable(getPayloadKey());
        }

        // 'take a picture' button action
        rootView.findViewById(R.id.takePictureBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        rootView.findViewById(R.id.cancelMarkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.goToFragment(new MapFragment());
            }
        });

        // 'upload' button action.
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBitmap == null) {
                    Toast.makeText(getContext(), "Debe tomar una foto!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Todo: This is just a placeholder. Here we should call a DAO and store mBitmap in Firebase.
                LoadingDialog loadingDialog = new LoadingDialog(activity, "Subiendo");
                loadingDialog.start();
                (new Handler()).postDelayed(() -> {
                    mark.setImageId(mBitmap.toString()); // Todo llamamos a la persistencia acá? Cuando le ponemos el userId?
                    mark.setText(markTagText.getText().toString());
                    loadingDialog.dismiss();
                    Toast.makeText(getContext(), "Mark creada!", Toast.LENGTH_SHORT).show();
                    activity.goToFragment(new MapFragment(), mark);
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
            }
        }
    }
}
