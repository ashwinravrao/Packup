package com.ashwinrao.boxray.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentListBinding;
import com.ashwinrao.boxray.util.ContextualAppBarListener;
import com.ashwinrao.boxray.util.LoadTruckDialog;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class ListFragment extends Fragment implements ContextualAppBarListener {

    private ListAdapter listAdapter;
    private List<Box> localBoxes;
    private LiveData<List<Box>> boxesLD;
    private FragmentListBinding binding;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        final BoxViewModelFactory factory = BoxViewModelFactory.getInstance(Objects.requireNonNull(getActivity()).getApplication());
        final BoxViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(BoxViewModel.class);
        boxesLD = viewModel.getBoxes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        BottomAppBar appBar = binding.bottomAppBar;
        ((MainActivity) Objects.requireNonNull(getActivity())).setSupportActionBar(appBar);

        final Toolbar toolbar = binding.defaultToolbar;
        toolbar.setTitle(R.string.toolbar_title_all);
        toolbar.inflateMenu(R.menu.menu_toolbar_list);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.toolbar_search) {
                    Snackbar.make(Objects.requireNonNull(getView()), "Searching", Snackbar.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        ExtendedFloatingActionButton fab = binding.fabExtended;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()));
        listAdapter.setToolbarTitleListener(this);
        recyclerView.setAdapter(listAdapter);

        boxesLD.observe(this, new Observer<List<Box>>() {
            @Override
            public void onChanged(List<Box> boxes) {
                if (boxes != null) {
                    localBoxes = new ArrayList<>(boxes);
                    listAdapter.setBoxes(boxes);
                } else {
                    listAdapter.setBoxes(new ArrayList<Box>());
                }
                recyclerView.setAdapter(listAdapter);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_bottom_bar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_bar_about:
                // todo: inflate "About" fragment layout
                return true;
            case R.id.bottom_bar_settings:
                // todo: start "Settings" activity
                return true;
        }
        return false;
    }

    @Override
    public void overlayContextualAppBar() {
        binding.contextualAppBar.setVisibility(View.VISIBLE);

        final Toolbar toolbar = binding.contextualToolbar;
        if (toolbar != null) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_contextual_list);
            toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.toolbar_load_truck:
                            new LoadTruckDialog().build(Objects.requireNonNull(getActivity()), R.layout.dialog_load_truck, R.string.dialog_title_save_address_load_truck, R.string.dialog_title_cancel_load_truck, loadTruckDialogPositiveClickListener(), loadTruckDialogNegativeClickListener());
                            return true;
                        case R.id.toolbar_delete:
                            Snackbar.make(Objects.requireNonNull(getView()),
                                    "Deleted",
                                    Snackbar.LENGTH_LONG).show();
                            return true;
                        default:
                            return false;
                    }
                }
            });

            final ImageView backArrow = toolbar.findViewById(R.id.back_arrow);
            if (backArrow != null) {
                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        restoreDefaultAppBar();
                        listAdapter.disableBulkEdit();
                        listAdapter.setBoxes(localBoxes);
                        recyclerView.setAdapter(listAdapter);
                    }
                });
            }
        }

    }

    private DialogInterface.OnClickListener loadTruckDialogPositiveClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(Objects.requireNonNull(getView()), "Saving", Snackbar.LENGTH_LONG).show();    // todo: replace with save address for geofencing
            }
        };
    }

    private DialogInterface.OnClickListener loadTruckDialogNegativeClickListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        };
    }

    @Override
    public void restoreDefaultAppBar() {
        binding.contextualAppBar.setVisibility(View.GONE);
    }


}
