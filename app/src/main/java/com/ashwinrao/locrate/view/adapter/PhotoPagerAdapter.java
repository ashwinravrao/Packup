package com.ashwinrao.locrate.view.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ashwinrao.locrate.util.callback.PaginationCallback;
import com.ashwinrao.locrate.view.pages.PhotoPage;

import java.util.List;

public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private int count;
    private List<String> paths;
    private PaginationCallback listener;

    public PhotoPagerAdapter(@NonNull FragmentManager fm, int count, @NonNull PaginationCallback listener, List<String> paths) {
        super(fm);
        this.count = count;
        this.listener = listener;
        this.paths = paths;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("path", paths.get(position));
        PhotoPage fragment = new PhotoPage();
        fragment.setArguments(bundle);
        fragment.registerPaginationCallback(listener);
        return fragment;
    }



    @Override
    public int getCount() {
        return count;
    }
}
