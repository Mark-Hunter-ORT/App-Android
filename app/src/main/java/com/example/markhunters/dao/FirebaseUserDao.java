package com.example.markhunters.dao;

import com.example.markhunters.model.UserModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FirebaseUserDao implements Dao<UserModel> {
    private final String USER_COLLECTION = "users";
    private final FirebaseFirestore fStore;
    private final CollectionReference dbCollection;
    private UserModel model;

    public FirebaseUserDao () {
        fStore = FirebaseFirestore.getInstance();
        dbCollection = fStore.collection(USER_COLLECTION);
        model = null;
    }

    @Override
    @Nullable
    public UserModel find(@NotNull final String uid) {
        final DocumentReference userReference = dbCollection.document(uid);
        userReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot user, @Nullable FirebaseFirestoreException error) {
                if (user != null && user.exists()) {
                    final String nickname = user.getString("nickname");
                    final String email = user.getString("email");
                    model = new UserModel(uid, nickname, email);
                }
            }
        });
        return model;
    }

    @Override
    public Task<Void> persist(@NotNull final UserModel model) {
        final UserModel persisted = find(model.getUid());
        if (persisted != null) {
            return update(model);// todo return update();
        } else {
            return create(model);
        }
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
