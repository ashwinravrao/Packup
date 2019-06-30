package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.databinding.ViewholderThumbnailBinding;
import com.ashwinrao.locrate.util.callback.CameraInitCallback;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class ThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int width;
    private int height;
    private Context context;
    private CameraInitCallback cameraListener;
    private List<String> paths = new ArrayList<>();

    public void registerStartCameraListener(CameraInitCallback listener) {
        this.cameraListener = listener;
    }

    public ThumbnailAdapter(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderThumbnailBinding binding  = ViewholderThumbnailBinding.inflate(LayoutInflater.from(context));
        return new ThumbnailVH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ThumbnailVH viewholder = (ThumbnailVH) holder;
        String path = paths.get(position);
        Glide.with(context)
                .load(new File(path))
                .thumbnail(0.01f)  // downsample to 1% of original image resolution for thumbnail
                .override(width + dpToPx(context, 16f),
                        height)
                .centerCrop()
                .into(viewholder.binding.thumbnail);
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
