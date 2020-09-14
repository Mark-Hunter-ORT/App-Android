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
                    callback.onCallback(new UserModel(uid, nickname, email));
                } else {
                    callback.onCallback(null);
                }
            }
        });
    }

    @Override
    public void persist(@NotNull final UserModel model, DaoCallback<UserModel> callback) {
        find(model.getUid(), new DaoCallback<UserModel>() {
            @Override
            public void onCallback(@Nullable final UserModel persisted) {
                if (persisted != null) {
                    update(model, callback);
                } else {
                    create(model, callback);
                }
            }
        });
    }

    @Override
    public void update(@NotNull final UserModel model, DaoCallback<UserModel> callback) {
        final String uid = model.getUid();
        final DocumentReference userReference = fStore.collection("users").document(uid);
        userReference.update("nickname", model.getNickname()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                find(model.getUid(), callback);
            }
        });
    }

    @Override
    public void create(@NotNull final UserModel model, DaoCallback<UserModel> callback) {
        final String uid = model.getUid();
        final DocumentReference userReference = fStore.collection("users").document(uid);
        userReference.set(model.toDto()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                find(model.getUid(), callback);
            }
        });
    }

}
