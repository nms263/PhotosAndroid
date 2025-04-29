package com.example.photosandroid.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photosandroid.R;
import com.example.photosandroid.models.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    public interface OnPhotoClickListener {
        void onPhotoClick(Photo photo);
        void onPhotoLongClick(Photo photo);
    }

    private List<Photo> photos;
    private OnPhotoClickListener listener;

    public PhotoAdapter(List<Photo> photos, OnPhotoClickListener listener) {
        this.photos = photos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Photo photo = photos.get(position);

        // For now, load from file path using a basic method (no external libs)
        holder.thumbnail.setImageURI(android.net.Uri.parse(photo.getFilePath()));

        holder.itemView.setOnClickListener(v -> listener.onPhotoClick(photo));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onPhotoLongClick(photo);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.photo_thumbnail);
        }
    }
}
