package com.ashwinrao.boxray.view.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.FragmentNumberPageFourBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class NumberPageFourFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentNumberPageFourBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_number_page_four, container, false);

        configureToolbar(binding.toolbar);

        return binding.getRoot();
    }

    private void configureToolbar(@NonNull Toolbar toolbar) {
        toolbar.setTitle(getString(R.string.title_page_four_number));
        toolbar.inflateMenu(R.menu.menu_toolbar_page_four);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.toolbar_show_me:
                Snackbar.make(Objects.requireNonNull(getView()), "Help is on the way", Snackbar.LENGTH_LONG).show();   // todo replace with some other action
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
