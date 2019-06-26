package com.ashwinrao.locrate.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentPhotoReviewBinding;
import com.ashwinrao.locrate.util.PaginationCallback;
import com.ashwinrao.locrate.view.adapter.PhotoReviewPagerAdapter;
import com.ashwinrao.locrate.viewmodel.PhotoViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class PhotoReviewFragment extends Fragment implements Toolbar.OnMenuItemClickListener, PaginationCallback {

    private ViewPager viewPager;
    private PhotoReviewPagerAdapter adapter;
    private List<String> paths = new ArrayList<>();

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Locrate) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), android.R.color.black));
        final PhotoViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(PhotoViewModel.class);
        paths.addAll(viewModel.getPaths());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentPhotoReviewBinding binding = FragmentPhotoReviewBinding.inflate(inflater);
        setupToolbar(binding.toolbar);
        setupViewPager(binding.viewPager);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Objects.requireNonNull(getActivity()).getWindow().setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
        toolbar.inflateMenu(R.menu.photo_review);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(v -> {
            createUnsavedChangesDialog(getContext()).show();
        });
    }

    private AlertDialog createUnsavedChangesDialog(Context context) {
        return new AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_exit_photo_review_title))
                .setMessage(getString(R.string.dialog_exit_photo_review_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.yes), (dialog1, which) -> {
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialog12, which) -> dialog12.cancel())
                .create();
    }

    private void setupViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
        adapter = new PhotoReviewPagerAdapter(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), paths.size(), this, paths);
        viewPager.setAdapter(adapter);
    }

    private void finishUp() {
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.toolbar_done) {
            finishUp();
            return true;
        }
        return false;
    }

    @Override
    public void progress() {
        int nextItem = viewPager.getCurrentItem() == adapter.getCount() ? viewPager.getCurrentItem() : viewPager.getCurrentItem()+1;
        viewPager.setCurrentItem(nextItem);
    }

    @Override
    public void regress() {
        int previousItem = viewPager.getCurrentItem() == 0 ? 0 : viewPager.getCurrentItem()-1;
        viewPager.setCurrentItem(previousItem);
    }
}
