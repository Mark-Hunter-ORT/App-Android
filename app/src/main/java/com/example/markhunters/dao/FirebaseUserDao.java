package com.example.markhunters.dao;

import androidx.annotation.NonNull;

import com.example.markhunters.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FirebaseUserDao implements Dao<UserModel> {
    private final String USER_COLLECTION = "users";
    private final FirebaseFirestore fStore;
    private final CollectionReference dbCollection;

    public FirebaseUserDao () {
        fStore = FirebaseFirestore.getInstance();
        dbCollection = fStore.collection(USER_COLLECTION);
    }

    @Override
    public void find(@NotNull final String uid, DaoCallback<UserModel> callback) {
        final DocumentReference userReference = dbCollection.document(uid);
        userReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot user = task.getResult();
                if (user != null && user.exists()) {
                    final String nickname = user.getString("nickname");
                    final String email = user.getString("email");
                    callback.onActionCallback(new UserModel(uid, nickname, email));
                } else {
                    callback.onActionCallback(null);
                }
            }
        });
    }

    @Override
    public void persist(@NotNull final UserModel model, DaoCallback<UserModel> callback) {
        find(model.getUid(), new DaoCallback<UserModel>() {
            @Override
            public void onActionCallback(@Nullable final UserModel persisted) {
                if (persisted != null) {
                    callback.onTaskCallback(update(model));
                } else {
                    callback.onTaskCallback(create(model));
                }
            }
        });
    }

    @Override
    public Task<Void> update(@NotNull final UserModel model) {
        final String uid = model.getUid();
        final DocumentReference userReference = fStore.collection("users").document(uid);
        return userReference.update("nickname", model.getNickname());
    }

    @Override
    public Task<Void> create(@NotNull final UserModel model) {
        final String uid = model.getUid();
        final DocumentReference userReference = fStore.collection("users").document(uid);
        return userReference.set(model.toDto());
    }

}
