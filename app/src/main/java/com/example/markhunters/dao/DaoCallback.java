package com.example.markhunters.dao;

import com.google.android.gms.tasks.Task;

public interface DaoCallback <T> {
    default void onActionCallback (T model) {}
    default void onTaskCallback (Task<Void> task) {}
}
