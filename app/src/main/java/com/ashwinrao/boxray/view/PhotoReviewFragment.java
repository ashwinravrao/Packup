package com.ashwinrao.boxray.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentPhotoReviewBinding;
import com.ashwinrao.boxray.view.adapter.PhotoReviewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PhotoReviewFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private List<String> paths = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            if(getArguments().getStringArrayList("paths") != null) {
                paths.addAll(getArguments().getStringArrayList("paths"));
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentPhotoReviewBinding binding = FragmentPhotoReviewBinding.inflate(inflater);
        setupToolbar(binding.toolbar);
        setupViewPager(binding.viewPager);
        return binding.getRoot();
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.photo_review);
        toolbar.setOnMenuItemClickListener(this);
    }

    private void setupViewPager(ViewPager viewPager) {
        final PhotoReviewAdapter adapter = new PhotoReviewAdapter(getContext(), paths);
        viewPager.setAdapter(adapter);
    }

    private void finishUp() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_skip) {
            finishUp();
            return true;
        }
        return false;
    }
}
