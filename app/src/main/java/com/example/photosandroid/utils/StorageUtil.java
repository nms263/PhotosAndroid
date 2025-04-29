package com.example.photosandroid.utils;

import android.content.Context;

import com.example.photosandroid.models.Album;
import com.example.photosandroid.models.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class StorageUtil {

    private static final String FILE_NAME = "albums.json";

    // Save albums list to JSON file
    public static void saveAlbums(Context context, List<Album> albums) {
        JSONArray albumArray = new JSONArray();
        try {
            for (Album album : albums) {
                JSONObject albumObj = new JSONObject();
                albumObj.put("name", album.getName());

                JSONArray photoArray = new JSONArray();
                for (Photo photo : album.getPhotos()) {
                    JSONObject photoObj = new JSONObject();
                    photoObj.put("filePath", photo.getFilePath());

                    JSONObject tagsObj = new JSONObject(photo.getTags());
                    photoObj.put("tags", tagsObj);

                    photoArray.put(photoObj);
                }

                albumObj.put("photos", photoArray);
                albumArray.put(albumObj);
            }

            File file = new File(context.getFilesDir(), FILE_NAME);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(albumArray.toString());
            writer.close();

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    // Load albums list from JSON file
    public static List<Album> loadAlbums(Context context) {
        List<Album> albums = new ArrayList<>();
        File file = new File(context.getFilesDir(), FILE_NAME);

        if (!file.exists()) {
            return albums; // Empty list if no file exists yet
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            reader.close();

            JSONArray albumArray = new JSONArray(builder.toString());
            for (int i = 0; i < albumArray.length(); i++) {
                JSONObject albumObj = albumArray.getJSONObject(i);
                String name = albumObj.getString("name");

                Album album = new Album(name);

                JSONArray photoArray = albumObj.getJSONArray("photos");
                for (int j = 0; j < photoArray.length(); j++) {
                    JSONObject photoObj = photoArray.getJSONObject(j);
                    String filePath = photoObj.getString("filePath");

                    Photo photo = new Photo(filePath);

                    JSONObject tagsObj = photoObj.getJSONObject("tags");
                    Iterator<String> keys = tagsObj.keys();
                    while (keys.hasNext()) {
                        String key = keys.next();
                        photo.addTag(key, tagsObj.getString(key));
                    }

                    album.addPhoto(photo);
                }

                albums.add(album);
            }

        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }

        return albums;
    }
}
