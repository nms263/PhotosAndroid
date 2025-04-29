package com.example.photosandroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosandroid.R;
import com.example.photosandroid.adapters.PhotoAdapter;
import com.example.photosandroid.models.Album;
import com.example.photosandroid.models.Photo;
import com.example.photosandroid.utils.StorageUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AlbumActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private RecyclerView photoList;
    private PhotoAdapter photoAdapter;
    private FloatingActionButton addPhotoButton;

    private List<Album> albums;
    private Album currentAlbum;
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        photoList = findViewById(R.id.photo_list);
        addPhotoButton = findViewById(R.id.add_photo_button);

        index = getIntent().getIntExtra("album_index", -1);
        albums = StorageUtil.loadAlbums(this);

        if (index >= 0 && index < albums.size()) {
            currentAlbum = albums.get(index);
            setTitle(currentAlbum.getName());
        } else {
            Toast.makeText(this, "Could not load album", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        photoAdapter = new PhotoAdapter(currentAlbum.getPhotos(), new PhotoAdapter.OnPhotoClickListener() {
            @Override
            public void onPhotoClick(Photo photo) {
                Toast.makeText(AlbumActivity.this, "Photo clicked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPhotoLongClick(Photo photo) {
                Toast.makeText(AlbumActivity.this, "Photo long clicked", Toast.LENGTH_SHORT).show();
            }
        });

        photoList.setLayoutManager(new GridLayoutManager(this, 2));
        photoList.setAdapter(photoAdapter);

        addPhotoButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                getContentResolver().takePersistableUriPermission(
                        imageUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                );

                Photo newPhoto = new Photo(imageUri.toString());
                currentAlbum.addPhoto(newPhoto);

                albums.set(index, currentAlbum);
                StorageUtil.saveAlbums(this, albums);

                photoAdapter.notifyItemInserted(currentAlbum.getPhotos().size() - 1);
                photoList.scrollToPosition(currentAlbum.getPhotos().size() - 1);

                Toast.makeText(this, "Photo added!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
