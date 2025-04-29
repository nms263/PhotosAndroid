package com.example.photosandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosandroid.R;
import com.example.photosandroid.adapters.AlbumAdapter;
import com.example.photosandroid.models.Album;
import com.example.photosandroid.utils.StorageUtil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView albumList;
    private FloatingActionButton addAlbumButton;
    private AlbumAdapter albumAdapter;
    private List<Album> albums;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        albumList = findViewById(R.id.album_list);
        addAlbumButton = findViewById(R.id.add_album_button);

        // Load albums from storage
        albums = StorageUtil.loadAlbums(this);
        if (albums == null) {
            albums = new ArrayList<>();
        }

        // Set up RecyclerView
        albumAdapter = new AlbumAdapter(albums, new AlbumAdapter.OnAlbumClickListener() {
            @Override
            public void onAlbumClick(Album album) {
                // TODO: Open AlbumActivity to view photos inside album
                Toast.makeText(HomeActivity.this, "Opening album: " + album.getName(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAlbumLongClick(Album album) {
                // TODO: Show options to rename or delete album
                Toast.makeText(HomeActivity.this, "Long pressed album: " + album.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        albumList.setLayoutManager(new LinearLayoutManager(this));
        albumList.setAdapter(albumAdapter);

        // Add album button
        addAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Step 1: Create a dummy album
                String newAlbumName = "Album " + (albums.size() + 1);
                Album newAlbum = new Album(newAlbumName);

                // Step 2: Add to the list
                albums.add(newAlbum);

                // Step 3: Save updated list
                StorageUtil.saveAlbums(HomeActivity.this, albums);

                // Step 4: Refresh RecyclerView
                albumAdapter.notifyItemInserted(albums.size() - 1);

                // Step 5: Optional feedback
                Toast.makeText(HomeActivity.this, "New Album Added: " + newAlbumName, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
