package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoReviewAdapter extends PagerAdapter {

    private Context context;
    private CircularProgressDrawable progress;
    private List<File> files = new ArrayList<>();   // load images faster with pre-created files

    public PhotoReviewAdapter(Context context, List<String> paths) {
        this.context = context;
        this.files = createFilesFromPaths(paths);
        this.progress = new CircularProgressDrawable(context);
    }

    private List<File> createFilesFromPaths(List<String> paths) {
        List<File> files = new ArrayList<>();
        for(String path : paths) {
            files.add(new File(path));
        }
        return files;
    }

    private void setupProgressIndicator() {
        this.progress.setTint(ContextCompat.getColor(context, android.R.color.white));
        this.progress.setStrokeWidth(5f);
        this.progress.setCenterRadius(30f);
        this.progress.start();
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        final ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        setupProgressIndicator();

        Glide.with(context)
                .load(files.get(position))
                .placeholder(progress)
                .into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
