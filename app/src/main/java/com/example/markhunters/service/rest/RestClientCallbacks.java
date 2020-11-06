package com.example.markhunters.service.rest;

import com.example.markhunters.model.Model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RestClientCallbacks {
    @FunctionalInterface
    public interface CallbackInstance<T extends Model> {
        void onCallback(@Nullable final T model);
    }

    @FunctionalInterface
    public interface CallbackCollection<T extends Model> {
        void onCallback(List<T> models);
    }

    @FunctionalInterface
    public interface CallbackVoid {
        void onCallback();
    }

}
