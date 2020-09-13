package com.example.markhunters.dao;

import com.google.android.gms.tasks.Task;

public interface PersistCallback<Void> {
    void onPersistCallback (Task<Void> task);
}
