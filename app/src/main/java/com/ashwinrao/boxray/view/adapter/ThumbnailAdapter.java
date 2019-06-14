package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.boxray.databinding.ViewholderAddItemBinding;
import com.ashwinrao.boxray.databinding.ViewholderThumbnailBinding;
import com.ashwinrao.boxray.util.CameraInitCallback;
import com.ashwinrao.boxray.util.Utilities;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int width;
    private int height;
    private Context context;
    private CameraInitCallback cameraListener;
    private List<String> paths = new ArrayList<>();

    private final int THUMBNAIL_VIEW_TYPE = 0;
    private final int BUTTON_VIEW_TYPE = 1;

    public void registerStartCameraListener(CameraInitCallback listener) {
        this.cameraListener = listener;
    }

    public ThumbnailAdapter(Context context, int width, int height) {
        this.context = context;
        this.width = width;
        this.height = height;
    }

    public void setPaths(List<String> paths) {
//        if (this.paths.size() > 0) {
//            this.paths.remove(this.paths.size() - 1);
//        }
        this.paths = paths;
//        this.paths.add("button");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderThumbnailBinding binding  = ViewholderThumbnailBinding.inflate(LayoutInflater.from(context));
        return new ThumbnailVH(binding);

//        if (viewType == THUMBNAIL_VIEW_TYPE) {
//            final ViewholderThumbnailBinding binding = ViewholderThumbnailBinding.inflate(LayoutInflater.from(context));
//            return new ThumbnailVH(binding);
//        } else {
//            final ViewholderAddItemBinding binding = ViewholderAddItemBinding.inflate(LayoutInflater.from(context));
//            return new AddItemVH(binding);
//        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ThumbnailVH viewholder = (ThumbnailVH) holder;
        String path = paths.get(position);
        Glide.with(context)
                .load(new File(path))
                .thumbnail(0.01f)  // downsample to 1% of original image resolution for thumbnail
                .override(width + Utilities.dpToPx(context, 16f),
                        height)
                .centerCrop()
                .into(viewholder.binding.thumbnail);

//        if (holder.getItemViewType() == THUMBNAIL_VIEW_TYPE) {
//            ThumbnailVH viewholder = (ThumbnailVH) holder;
//            String path = paths.get(position);
//            Glide.with(context)
//                    .load(new File(path))
//                    .thumbnail(0.01f)  // downsample to 1% of original image resolution for thumbnail
//                    .override(width + Utilities.dpToPx(context, 16f),
//                            height)
//                    .centerCrop()
//                    .into(viewholder.binding.thumbnail);
//        }
    }

//    @Override
//    public int getItemViewType(int position) {
//        if (position == paths.size() - 1) {
//            return BUTTON_VIEW_TYPE;
//        } else {
//            return THUMBNAIL_VIEW_TYPE;
//        }
//    }

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

//    public class AddItemVH extends RecyclerView.ViewHolder implements View.OnClickListener {
//
//        ViewholderAddItemBinding binding;
//
//        public AddItemVH(@NonNull ViewholderAddItemBinding binding) {
//            super(binding.getRoot());
//            this.binding = binding;
//            this.binding.getRoot().setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (cameraListener != null) {
//                cameraListener.startCamera();
//            }
//        }
//    }
}
