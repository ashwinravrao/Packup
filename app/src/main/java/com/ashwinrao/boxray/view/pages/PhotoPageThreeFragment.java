package com.ashwinrao.boxray.view.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentPhotoPageThreeBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class PhotoPageThreeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentPhotoPageThreeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo_page_three, container, false);

        configureToolbar(binding.toolbar);

        binding.photoPlaceholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(Objects.requireNonNull(getView()), "Opening camera intent ...", Snackbar.LENGTH_LONG).show(); // todo replace
            }
        });

        return binding.getRoot();
    }

    private void configureToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(getString(R.string.title_page_three_photo));
        toolbar.inflateMenu(R.menu.menu_toolbar_page_three);
        toolbar.setOnMenuItemClickListener(this);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_retake_photo:
                Snackbar.make(Objects.requireNonNull(getView()), "Camera intent goes here", Snackbar.LENGTH_LONG).show();   // todo replace with camera intent
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
