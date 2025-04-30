package com.example.photosandroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.photosandroid.R;
import com.example.photosandroid.models.Album;
import com.example.photosandroid.models.Photo;
import com.example.photosandroid.utils.StorageUtil;

import java.util.List;
import java.util.Map;

public class SlideshowActivity extends AppCompatActivity {

    private ImageView slideshowImage;
    private TextView tagDisplay;
    private Button previousButton, nextButton, addTagButton, deleteTagButton;
    private List<Photo> photoList;
    private List<Album> albums;
    private int currentIndex;
    private int albumIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        slideshowImage = findViewById(R.id.slideshow_image);
        tagDisplay = findViewById(R.id.tag_display);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);
        addTagButton = findViewById(R.id.add_tag_button);
        deleteTagButton = findViewById(R.id.delete_tag_button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        albumIndex = getIntent().getIntExtra("album_index", -1);
        int photoIndex = getIntent().getIntExtra("photo_index", 0);
        albums = StorageUtil.loadAlbums(this);

        if (albumIndex < 0 || albumIndex >= albums.size()) {
            finish();
            return;
        }

        photoList = albums.get(albumIndex).getPhotos();
        currentIndex = photoIndex;
        showPhoto(currentIndex);

        previousButton.setOnClickListener(v -> {
            if (currentIndex > 0) {
                currentIndex--;
                showPhoto(currentIndex);
            }
        });

        nextButton.setOnClickListener(v -> {
            if (currentIndex < photoList.size() - 1) {
                currentIndex++;
                showPhoto(currentIndex);
            }
        });

        addTagButton.setOnClickListener(v -> showAddTagDialog());
        deleteTagButton.setOnClickListener(v -> showDeleteTagDialog());
    }

    private void showPhoto(int index) {
        Photo photo = photoList.get(index);
        Uri uri = Uri.parse(photo.getFilePath());

        Log.d("SlideshowActivity", "Displaying URI: " + uri.toString());

        slideshowImage.setImageURI(uri);
        updateTagDisplay(photo);
    }

    private void updateTagDisplay(Photo photo) {
        Map<String, String> tags = photo.getTags();
        StringBuilder tagText = new StringBuilder("Tags: ");
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            tagText.append(entry.getKey()).append(" = ").append(entry.getValue()).append("  ");
        }
        tagDisplay.setText(tagText.toString());
    }

    private void showAddTagDialog() {
        final String[] types = {"person", "location"};

        new AlertDialog.Builder(this)
                .setTitle("Select tag type")
                .setItems(types, (dialog, which) -> {
                    String type = types[which];

                    final EditText input = new EditText(this);
                    input.setHint("Enter tag value");
                    input.setPadding(48, 32, 48, 32);

                    new AlertDialog.Builder(this)
                            .setTitle("Enter " + type)
                            .setView(input)
                            .setPositiveButton("Add", (d, w) -> {
                                String value = input.getText().toString().trim();
                                if (!value.isEmpty()) {
                                    Photo currentPhoto = photoList.get(currentIndex);
                                    currentPhoto.addTag(type, value);
                                    albums.set(albumIndex, albums.get(albumIndex));
                                    StorageUtil.saveAlbums(this, albums);
                                    updateTagDisplay(currentPhoto);
                                }
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                })
                .show();
    }

    private void showDeleteTagDialog() {
        Photo currentPhoto = photoList.get(currentIndex);
        Map<String, String> tags = currentPhoto.getTags();

        if (tags.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("No tags to delete")
                    .setMessage("This photo has no tags.")
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        String[] tagKeys = tags.keySet().toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("Select tag to delete")
                .setItems(tagKeys, (dialog, which) -> {
                    String tagToDelete = tagKeys[which];
                    currentPhoto.removeTag(tagToDelete);
                    albums.set(albumIndex, albums.get(albumIndex));
                    StorageUtil.saveAlbums(this, albums);
                    updateTagDisplay(currentPhoto);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
