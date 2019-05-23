package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentListBinding;
import com.ashwinrao.boxray.util.BackNavigationListener;
import com.ashwinrao.boxray.util.DestinationDialog;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;


public class ListFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener, BackNavigationListener {

    private ListAdapter listAdapter;
    private List<Box> localBoxes;
    private LiveData<List<Box>> boxesLD;
    private RecyclerView recyclerView;
    private DrawerLayout drawer;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        final BoxViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
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
        final FragmentListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        AppCompatActivity parent = ((MainActivity) getActivity());
        Toolbar toolbar = binding.includeAppBar.toolbar;
        toolbar.setTitle(getString(R.string.toolbar_title_all));
        if(parent != null) parent.setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        navigationView.setCheckedItem(R.id.home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        FloatingActionButton fab = binding.includeAppBar.fab;
        fab.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
        });

        recyclerView = binding.includeAppBar.recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        addItemDecoration(recyclerView);
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()));
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

    private void addItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int spanCount = 2;
                int spacing = (int) Utilities.dpToPx(Objects.requireNonNull(getActivity()), 16f);

                if (position >= 0) {
                    int column = position % spanCount;

                    outRect.left = spacing - column * spacing / spanCount;
                    outRect.right = (column + 1) * spacing / spanCount;

                    if (position < spanCount) {
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing;
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_toolbar_list, menu);

        MenuItem search = menu.findItem(R.id.toolbar_search);
        SearchView searchView = (SearchView) search.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search boxes");
        searchView.setPadding((int) Utilities.dpToPx(Objects.requireNonNull(getActivity()), -16f), 0, 0, 0);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()) {
            case R.id.toolbar_search_by_image:
                Toast.makeText(getActivity(), "Todo: implement searching by image (CV)", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.toolbar_sort:
                Toast.makeText(getActivity(), "Todo: implement sorting", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    private DialogInterface.OnClickListener dialogPositive() {
        return (dialog, which) -> {
            Snackbar.make(Objects.requireNonNull(getView()), "Saving", Snackbar.LENGTH_LONG).show();    // todo: replace with save address for geofencing
        };
    }

    private DialogInterface.OnClickListener dialogNegative() {
        return (dialog, which) -> dialog.cancel();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((MainActivity) Objects.requireNonNull(getActivity())).unregisterBackNavigationListener();
    }
}
