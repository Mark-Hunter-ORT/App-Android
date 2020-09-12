package com.example.markhunters.dao;

import com.example.markhunters.model.UserModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FirebaseUserDao implements Dao<UserModel> {
    private static Dao<UserModel> instance = null;
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
    public UserModel persist(@NotNull UserModel model) {
        final UserModel persisted = find(model.getUid());
        if (persisted != null) {
            // todo return update();
        } else {
            // todo return create()
        }
        return model; // placeholder
    }

    public static Dao<UserModel> getInstance() {
        if (instance == null) {
            instance = new FirebaseUserDao();
        }
        return instance;
    }
}
