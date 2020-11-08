package com.example.markhunters.service.dao;

import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackInstance;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackInstance2;
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
    public void find(@NotNull final String uid, CallbackInstance2<UserModel> callback) {
        final DocumentReference userReference = dbCollection.document(uid);
        userReference.get().addOnCompleteListener(task -> {
            DocumentSnapshot user = task.getResult();
            if (user != null && user.exists()) {
                final String nickname = user.getString("nickname");
                final String email = user.getString("email");
                callback.onSuccess(new UserModel(uid, nickname, email));
            } else {
                callback.onSuccess(null);
            }
        });
    }

    @Override
    protected void update(@NotNull final UserModel model, CallbackInstance2<UserModel> callback) {
        final String key = model.getKey();
        final DocumentReference userReference = fStore.collection("users").document(key);
        userReference.update("nickname", model.getNickname()).addOnCompleteListener(task -> find(key, callback));
    }

    @Override
    protected void create(@NotNull final UserModel model, CallbackInstance2<UserModel> callback) {
        final String key = model.getKey();
        final DocumentReference userReference = fStore.collection("users").document(key);
        userReference.set(model.toDto()).addOnCompleteListener(task -> find(key, callback));
    }

}
