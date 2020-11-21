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
import com.example.markhunters.ui.LoadingDialog;

import org.jetbrains.annotations.NotNull;


public class MarkViewFragment extends MarkFragment {

    private final String markId;
    private ImageView mImageView;
    private TextView mTextView;
    private String authorId;
    private String authorName;

    public MarkViewFragment(@NotNull final String markId) {
        this.markId = markId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_mark_view, container, false);
        mImageView = root.findViewById(R.id.markViewImage);
        mTextView = root.findViewById(R.id.markViewText);
        LoadingDialog loadingDialog = new LoadingDialog(activity, "Subiendo");
        loadingDialog.start();
        getClient().getMark(markId, new RestClientCallbacks.CallbackInstance<Mark>() {
            @Override
            public void onFailure(@Nullable String message) {
                System.out.println(message);
                activity.runOnUiThread(() -> toast("Ocurrió un error cargando la Mark"));
                loadingDialog.dismiss();
            }

            @Override
            public void onSuccess(@Nullable Mark mark) {
                String imageUrl = mark.content.images.get(0);
                new ImageUtils.DownloadImageTask(mImageView).execute(imageUrl);
                mTextView.setText(mark.getTitle());
                authorId = mark.userId;
                authorName = "mark.userName";
                loadingDialog.dismiss();
            }
        });

        root.findViewById(R.id.followBtn).setOnClickListener(view -> {
            if (authorId != null) {
                loadingDialog.start();
                getClient().followUser(authorId, new RestClientCallbacks.CallbackAction() {
                   @Override
                   public void onSuccess() {
                       // tengo que hacer esto para que el mapa me muestre los marks de mis seguidores
                       // Puede ser asincrónico, y hasta quizás cacheado.
                       addFollowed(authorId, authorName); //todo esto no va a hacer falta cuando esté el restclient
                       activity.runOnUiThread(() -> toast("Siguiendo nuevo usuario!"));
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

        return root;
    }
}