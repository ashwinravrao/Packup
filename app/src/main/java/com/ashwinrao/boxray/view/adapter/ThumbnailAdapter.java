package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.ViewholderThumbnailBinding;
import com.ashwinrao.boxray.util.Utilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailVH> {

    private int width;
    private int height;
    private Context context;
    private List<String> paths = new ArrayList<>();

    public ThumbnailAdapter(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
    }

    public void setPaths(List<String> paths) {
        this.paths.addAll(paths);
    }

    @NonNull
    @Override
    public ThumbnailVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderThumbnailBinding binding = ViewholderThumbnailBinding.inflate(LayoutInflater.from(context));
        return new ThumbnailVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ThumbnailVH holder, int position) {
        String path = paths.get(position);
        Glide.with(context)
                .load(new File(path))
                .thumbnail(0.005f)  // downsample to 0.05% original image resolution for thumbnail
                .override(width + Utilities.dpToPx(context, 16f),  // wider to account for RV decoration
                        height)
                .centerCrop()   // for proper scaling in the receiving view
                .into(holder.binding.thumbnail);
    }

    @Override
    public int getItemCount() {
        return paths == null ? 0 : paths.size();
    }

    public class ThumbnailVH extends RecyclerView.ViewHolder {

        ViewholderThumbnailBinding binding;

        public ThumbnailVH(@NonNull ViewholderThumbnailBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
