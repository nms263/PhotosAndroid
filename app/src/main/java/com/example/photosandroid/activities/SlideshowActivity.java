package com.example.photosandroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.photosandroid.R;
import com.example.photosandroid.models.Album;
import com.example.photosandroid.models.Photo;
import com.example.photosandroid.utils.StorageUtil;

import java.util.List;

public class SlideshowActivity extends AppCompatActivity {

    private ImageView slideshowImage;
    private Button previousButton, nextButton;
    private List<Photo> photoList;
    private int currentIndex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);

        slideshowImage = findViewById(R.id.slideshow_image);
        previousButton = findViewById(R.id.previous_button);
        nextButton = findViewById(R.id.next_button);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        int albumIndex = getIntent().getIntExtra("album_index", -1);
        int photoIndex = getIntent().getIntExtra("photo_index", 0);
        List<Album> albums = StorageUtil.loadAlbums(this);

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
    }

    private void showPhoto(int index) {
        Photo photo = photoList.get(index);
        Uri uri = Uri.parse(photo.getFilePath());

        Log.d("SlideshowActivity", "Displaying URI: " + uri.toString());

        try {
            getContentResolver().takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );
        } catch (SecurityException e) {
            Log.w("SlideshowActivity", "Permission already granted or not needed.");
        }

        slideshowImage.setImageURI(uri);
    }
}