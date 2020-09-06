package com.example.markhunters.dao;

import com.example.markhunters.model.UserModel;

import org.jetbrains.annotations.NotNull;

public class UserDao implements Dao<UserModel> {
    private static Dao<UserModel> instance = null;
    @Override
    public UserModel find(@NotNull final String key) {
        return new UserModel(key, "SampleName", "SampleEmail");
    }

    @Override
    public UserModel persist(@NotNull UserModel model) {
        final UserModel persisted = find(model.getGoogle_id());
        if (persisted != null) {
            // todo return update();
        } else {
            // todo return create()
        }
        return model; // placeholder
    }

    public static Dao<UserModel> getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }
}
