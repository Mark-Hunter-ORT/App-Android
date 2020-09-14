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

public class FirebaseUserDao extends Dao<UserModel> {
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
    protected void update(@NotNull final UserModel model, DaoCallback<UserModel> callback) {
        final String key = model.getKey();
        final DocumentReference userReference = fStore.collection("users").document(key);
        userReference.update("nickname", model.getNickname()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                find(key, callback);
            }
        });
    }

    @Override
    protected void create(@NotNull final UserModel model, DaoCallback<UserModel> callback) {
        final String key = model.getKey();
        final DocumentReference userReference = fStore.collection("users").document(key);
        userReference.set(model.toDto()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                find(key, callback);
            }
        });
    }

}
