package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentListBinding;
import com.ashwinrao.boxray.util.BackNavigationListener;
import com.ashwinrao.boxray.util.ContextualAppBarListener;
import com.ashwinrao.boxray.util.DestinationDialog;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;


public class ListFragment extends Fragment implements ContextualAppBarListener, BackNavigationListener {

    private ListAdapter listAdapter;
    private List<Box> localBoxes;
    private LiveData<List<Box>> boxesLD;
    private FragmentListBinding binding;
    private RecyclerView recyclerView;
    private boolean inMultiSelectMode;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        final BoxViewModel viewModel = ViewModelProviders.of(getActivity(), factory).get(BoxViewModel.class);
        boxesLD = viewModel.getBoxes();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
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
        toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.toolbar_search) {
                    Snackbar.make(Objects.requireNonNull(getView()), "Searching", Snackbar.LENGTH_LONG).show();
                    return true;
                }
                return false;
        });

        ExtendedFloatingActionButton fab = binding.fabExtended;
        fab.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()));
        listAdapter.registerToolBarListener(this);
        recyclerView.setAdapter(listAdapter);

        boxesLD.observe(this, boxes -> {
            if (boxes != null) {
                localBoxes = new ArrayList<>(boxes);
                listAdapter.setBoxes(boxes);
            } else {
                listAdapter.setBoxes(new ArrayList<Box>());
            }
            recyclerView.setAdapter(listAdapter);
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
            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.toolbar_load_truck:
                        buildDestinationDialog();
                        return true;
                    case R.id.toolbar_delete:
                        Snackbar.make(Objects.requireNonNull(getView()),
                                "Deleted",
                                Snackbar.LENGTH_LONG).show();
                        return true;
                    default:
                        return false;
                }
            });

            final ImageView backArrow = toolbar.findViewById(R.id.back_arrow);
            if (backArrow != null) {
                backArrow.setOnClickListener(v -> resetMultiSelectMode());
            }
        }

    }

    private void buildDestinationDialog() {
        DestinationDialog dialog = new DestinationDialog(getActivity(), R.style.AppTheme_DialogButtons);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.dialog_load_truck_positive), dialogPositive());
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dialog_load_truck_negative), dialogNegative());
        dialog.create();
        dialog.show();
        modifyDialog(dialog);
    }

    private void modifyDialog(DestinationDialog dialog) {
        Objects.requireNonNull(dialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()));
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorAccent, Objects.requireNonNull(getActivity()).getTheme()));
    }

    private void resetMultiSelectMode() {
        restoreDefaultAppBar();
        listAdapter.disableMultiSelectMode();
        listAdapter.setBoxes(localBoxes);
        recyclerView.setAdapter(listAdapter);
    }


    private DialogInterface.OnClickListener dialogPositive() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Snackbar.make(Objects.requireNonNull(getView()), "Saving", Snackbar.LENGTH_LONG).show();    // todo: replace with save address for geofencing
            }
        };
    }

    private DialogInterface.OnClickListener dialogNegative() {
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


    @Override
    public void onBackPressed() {
        resetMultiSelectMode();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) Objects.requireNonNull(getActivity())).unregisterBackNavigationListener();
    }
}
