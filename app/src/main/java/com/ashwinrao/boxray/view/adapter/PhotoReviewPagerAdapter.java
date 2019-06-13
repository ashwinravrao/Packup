package com.ashwinrao.boxray.view.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.ashwinrao.boxray.util.PaginationCallback;
import com.ashwinrao.boxray.view.ReviewPageFragment;

public class PhotoReviewPagerAdapter extends FragmentStatePagerAdapter {

    private int count;
    private PaginationCallback listener;

    public PhotoReviewPagerAdapter(@NonNull FragmentManager fm, int count, @NonNull PaginationCallback listener) {
        super(fm);
        this.count = count;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        ReviewPageFragment fragment = new ReviewPageFragment();
        fragment.setArguments(bundle);
        fragment.registerPaginationCallback(listener);
        return fragment;
    }



    @Override
    public int getCount() {
        return count;
    }
}
