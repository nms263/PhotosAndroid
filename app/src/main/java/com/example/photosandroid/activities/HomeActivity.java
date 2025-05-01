package com.example.photosandroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        albums = StorageUtil.loadAlbums(this);
        if (albums == null) {
            albums = new ArrayList<>();
        }

        albumAdapter = new AlbumAdapter(albums, new AlbumAdapter.OnAlbumClickListener() {
            @Override
            public void onAlbumClick(Album album) {
                int index = albums.indexOf(album);
                Intent intent = new Intent(HomeActivity.this, AlbumActivity.class);
                intent.putExtra("album_index", index);
                startActivity(intent);
            }

            @Override
            public void onAlbumLongClick(Album album) {
                int index = albums.indexOf(album);
                String[] options = {"Rename", "Delete"};

                new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Album Options")
                        .setItems(options, (dialog, which) -> {
                            if (which == 0) {
                                final EditText input = new EditText(HomeActivity.this);
                                input.setHint("Enter new album name");

                                new AlertDialog.Builder(HomeActivity.this)
                                        .setTitle("Rename Album")
                                        .setView(input)
                                        .setPositiveButton("Rename", (d, w) -> {
                                            String newName = input.getText().toString().trim();
                                            if (!newName.isEmpty()) {
                                                album.setName(newName);
                                                StorageUtil.saveAlbums(HomeActivity.this, albums);
                                                albumAdapter.notifyItemChanged(index);
                                                Toast.makeText(HomeActivity.this, "Album renamed", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            } else if (which == 1) {
                                new AlertDialog.Builder(HomeActivity.this)
                                        .setTitle("Confirm Delete")
                                        .setMessage("Are you sure you want to delete this album?")
                                        .setPositiveButton("Delete", (d, w) -> {
                                            albums.remove(index);
                                            StorageUtil.saveAlbums(HomeActivity.this, albums);
                                            albumAdapter.notifyItemRemoved(index);
                                            Toast.makeText(HomeActivity.this, "Album deleted", Toast.LENGTH_SHORT).show();
                                        })
                                        .setNegativeButton("Cancel", null)
                                        .show();
                            }
                        })
                        .show();
            }
        });

        albumList.setLayoutManager(new LinearLayoutManager(this));
        albumList.setAdapter(albumAdapter);

        addAlbumButton.setOnClickListener(v -> {
            final EditText input = new EditText(HomeActivity.this);
            input.setHint("Enter album name");

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Add New Album")
                    .setView(input)
                    .setPositiveButton("Add", (dialog, which) -> {
                        String albumName = input.getText().toString().trim();
                        if (!albumName.isEmpty()) {
                            Album newAlbum = new Album(albumName);
                            albums.add(newAlbum);
                            StorageUtil.saveAlbums(HomeActivity.this, albums);
                            albumAdapter.notifyItemInserted(albums.size() - 1);
                            albumList.scrollToPosition(albums.size() - 1);
                            Toast.makeText(HomeActivity.this, "Album added: " + albumName, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "Album name cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }
}
