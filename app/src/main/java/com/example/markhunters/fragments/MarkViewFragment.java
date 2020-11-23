package com.example.markhunters.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.markhunters.R;
import com.example.markhunters.model.Mark;
import com.example.markhunters.service.image.ImageUtils;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.ui.LoadingDialog;

import org.jetbrains.annotations.NotNull;


public class MarkViewFragment extends MarkFragment {

    private final String markId;
    private ImageView mImageView;
    private TextView mTextView;
    private TextView authorTextView;
    private String authorId;
    private String authorName;
    private boolean isByFollowed;

    public MarkViewFragment(@NotNull final String markId) {
        this.markId = markId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mark_view, container, false);
        mImageView = root.findViewById(R.id.markViewImage);
        mTextView = root.findViewById(R.id.markViewText);
        authorTextView = root.findViewById(R.id.authorTextView);
        LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.start();
        getClient().getMark(markId, new RestClientCallbacks.CallbackInstance<Mark>() {
            @Override
            public void onFailure(@Nullable String message) {
                System.out.println(message);
                activity.runOnUiThread(() -> toast("Ocurrió un error cargando la Mark"));
                loadingDialog.dismiss();
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(@Nullable Mark mark) {
                activity.runOnUiThread(() -> {
                    String imageUrl = mark.content.images.get(0);
                    new ImageUtils.DownloadImageTask(mImageView).execute(imageUrl);
                    mTextView.setText(mark.getTitle());
                    authorTextView.setText("Autor: " + mark.userName);
                    authorId = mark.userId;
                    authorName = mark.userName;
                    isByFollowed = mark.isByFollowed;
                    resolveFollowButton(root);
                    loadingDialog.dismiss();
                });
            }
        });
        return root;
    }

    private void resolveFollowButton(View root) {
        View followBtn = root.findViewById(R.id.followBtn);
        if (isByFollowed) {
            followBtn.setVisibility(View.INVISIBLE);
            followBtn.setEnabled(false);
        } else followBtn.setOnClickListener(view -> {
                LoadingDialog loadingDialog = new LoadingDialog(activity, "Siguiendo...");
                loadingDialog.start();
                if (authorId != null) {
                    loadingDialog.start();
                    getClient().followUser(authorId, new RestClientCallbacks.CallbackAction() {
                        @Override
                        public void onSuccess() {
                            activity.runOnUiThread(() -> toast("Empezaste a seguir a " + authorName));
                            backToMap();
                        }

                        @Override
                        public void onFailure(@Nullable String message) {
                            System.out.println(message);
                            activity.runOnUiThread(() -> toast("Ocurrió un error intentando seguir al usuario"));
                            backToMap();
                        }

                        private void backToMap() {
                            loadingDialog.dismiss();
                            goToFragment(new MapFragment());
                        }
                    });
                }
            });
    }
}