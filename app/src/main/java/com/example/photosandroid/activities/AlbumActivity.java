package com.example.photosandroid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

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
                int photoIndex = currentAlbum.getPhotos().indexOf(photo);
                Intent intent = new Intent(AlbumActivity.this, SlideshowActivity.class);
                intent.putExtra("album_index", index);
                intent.putExtra("photo_index", photoIndex);
                startActivity(intent);            }

            @Override
            public void onPhotoLongClick(Photo photo) {
                new androidx.appcompat.app.AlertDialog.Builder(AlbumActivity.this)
                        .setTitle("Delete Photo")
                        .setMessage("Are you sure you want to delete this photo?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            int pos = currentAlbum.getPhotos().indexOf(photo);
                            if (pos != -1) {
                                currentAlbum.removePhoto(photo);
                                albums.set(index, currentAlbum);
                                StorageUtil.saveAlbums(AlbumActivity.this, albums);
                                photoAdapter.updatePhotos(currentAlbum.getPhotos());
                                Toast.makeText(AlbumActivity.this, "Photo deleted", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.album_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            startActivity(new Intent(this, SearchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 1) Reload from persistent storage
        albums = StorageUtil.loadAlbums(this);

        // 2) Grab the current album again
        if (index >= 0 && index < albums.size()) {
            currentAlbum = albums.get(index);

            // 3) Update adapter’s list
            photoAdapter.updatePhotos(currentAlbum.getPhotos());
        } else {
            // (optional) handle case where album was deleted
            finish();
        }
    }


}
