package com.example.markhunters.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;

public abstract class Model implements Serializable {
    /**
     * Builds the object that will be saved as a document in the database.
     * Do not include key/id value as it is set separately.
     */
    // public abstract Map<String, Object> toDto();
    // public abstract String getKey();
    public abstract JSONObject toJson() throws JSONException;
}
