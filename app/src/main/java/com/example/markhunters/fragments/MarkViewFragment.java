package com.example.markhunters.fragments;

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

import org.jetbrains.annotations.NotNull;


public class MarkViewFragment extends MarkFragment {

    private final String markId;
    private ImageView mImageView;
    private TextView mTextView;

    public MarkViewFragment(@NotNull final String markId) {
        this.markId = markId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mark_view, container, false);
        mImageView = root.findViewById(R.id.markViewImage);
        mTextView = root.findViewById(R.id.markViewText);
        getClient().getMark(markId, new RestClientCallbacks.CallbackInstance<Mark>() {
            @Override
            public void onFailure(@Nullable String message) {
                System.out.println(message);
                activity.runOnUiThread(() -> toast("Ocurrió un error cargando la Mark"));
            }

            @Override
            public void onSuccess(@Nullable Mark mark) {
                String imageUrl = mark.content.images.get(0);
                new ImageUtils.DownloadImageTask(mImageView).execute(imageUrl);
                mTextView.setText(mark.getTitle());
            }
        });

        return root;
    }
}