package com.example.markhunters.service.rest;

import androidx.annotation.Nullable;

import com.example.markhunters.model.Model;


import java.util.List;

public class RestClientCallbacks {
    public interface CallbackInstance<T extends Model> extends Fallible {
        void onSuccess(@Nullable final T model);
    }

    public interface CallbackCollection<T extends Model> extends Fallible {
        void onSuccess(List<T> models);
    }
    
    public interface CallbackAction extends Fallible {
        void onSuccess();
    }

    public interface Fallible {
        void onFailure(@Nullable String message);
        default void onError (@Nullable String message, int code) {}
    }

}
