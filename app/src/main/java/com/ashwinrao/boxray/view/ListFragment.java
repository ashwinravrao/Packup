package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentListBinding;
import com.ashwinrao.boxray.util.BackNavCallback;
import com.ashwinrao.boxray.view.adapter.ListAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;

import javax.inject.Inject;

import static com.ashwinrao.boxray.util.UnitConversion.dpToPx;


public class ListFragment extends Fragment implements BackNavCallback {

    private Toolbar toolbar;
    private ListAdapter listAdapter;
    private LiveData<List<Box>> boxesLD;
    private ExtendedFloatingActionButton fab;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) Objects.requireNonNull(getActivity())).registerBackNavigationListener(this);
        final BoxViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        boxesLD = viewModel.getBoxes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentListBinding binding = FragmentListBinding.inflate(inflater);
        AppCompatActivity parent = ((MainActivity) getActivity());
        setupToolbar(Objects.requireNonNull(parent), binding.toolbar);
        setupRecyclerView(binding.recyclerView);
        setupEFAB(binding.fab);
        binding.bottomAppBar.setNavigationOnClickListener(view -> inflateBottomSheet());
        return binding.getRoot();
    }

    private void inflateBottomSheet() {
        final BottomSheetFragment bottomSheet = new BottomSheetFragment();
        bottomSheet.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), bottomSheet.getTag());
    }

    private void setupEFAB(@NonNull ExtendedFloatingActionButton efab) {
        fab = efab;
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
            v.setEnabled(false);
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(recyclerView);
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()));
        recyclerView.setAdapter(listAdapter);
        boxesLD.observe(this, boxes -> {
            if(boxes != null) {
                listAdapter.setBoxes(boxes);
            } else {
                listAdapter.setBoxes(new ArrayList<>());
            }
            recyclerView.setAdapter(listAdapter);
        });
    }

    private void setupToolbar(@NonNull AppCompatActivity parent, @NonNull Toolbar toolbar) {
        this.toolbar = toolbar;
        toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_overflow, Objects.requireNonNull(getActivity()).getTheme()));
        toolbar.setTitle(getString(R.string.toolbar_title_all));
        parent.setSupportActionBar(toolbar);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!fab.isEnabled()) {
            fab.setEnabled(true);
        }
    }

    private void addItemDecoration(RecyclerView recyclerView) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int spanCount = 1;
                int spacing = dpToPx(Objects.requireNonNull(getActivity()), 16f);

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
        configureSearchView(searchView);
    }

    private void configureSearchView(SearchView searchView) {
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search boxes");
        searchView.setPadding(dpToPx(Objects.requireNonNull(getActivity()), -16f), 0, 0, dpToPx(getActivity(), -1f));
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

//        switch(item.getItemId()) {
//            case R.id.toolbar_scan:
//                Toast.makeText(getActivity(), "Todo: implement searching by image (CV)", Toast.LENGTH_SHORT).show();
//                return true;
//            case R.id.toolbar_sort:
//                Toast.makeText(getActivity(), "Todo: implement sorting", Toast.LENGTH_SHORT).show();
//                return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((MainActivity) Objects.requireNonNull(getActivity())).unregisterBackNavigationListener();
    }
}
