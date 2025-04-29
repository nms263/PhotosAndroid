package com.example.photosandroid.models;

import java.util.HashMap;
import java.util.Map;

public class Photo {

    private String filePath;
    private final Map<String, String> tags; // Example: { "person": "John", "location": "New York" }

    public Photo(String filePath) {
        this.filePath = filePath;
        this.tags = new HashMap<>();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void addTag(String type, String value) {
        tags.put(type.toLowerCase(), value);
    }

    public void removeTag(String type) {
        tags.remove(type.toLowerCase());
    }
}