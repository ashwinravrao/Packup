package com.ashwinrao.locrate.view.fragment.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.FragmentPageBoxesBinding;
import com.ashwinrao.locrate.util.callback.DismissCallback;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.activity.NfcActivity;
import com.ashwinrao.locrate.view.adapter.BoxesAdapter;
import com.ashwinrao.locrate.view.fragment.CategoryFilterDialog;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class BoxesPage extends Fragment implements DismissCallback {

    private int numBoxes;
    private RecyclerView recyclerView;
    private BoxesAdapter boxesAdapter;
    private LiveData<List<Box>> boxesLD;
    private FragmentPageBoxesBinding binding;
    private FloatingActionButton[] fabs;
    private UpdateActionModeCallback callback;
    private List<Box> boxes = new ArrayList<>();

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
        final BoxViewModel boxViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        boxesLD = boxViewModel.getBoxes();
    }

    @Override
    public void onResume() {
        super.onResume();
        for (FloatingActionButton fab : fabs) fab.setEnabled(true);
        if(binding != null) setFilterActivated(false);
        if (boxesAdapter != null) {
            boxesAdapter.initializeFilter();
            if(boxes != null && recyclerView != null) {
                boxesAdapter.setBoxes(boxes);
                recyclerView.setAdapter(boxesAdapter);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageBoxesBinding.inflate(inflater);

        // binding vars
        setFilterActivated(false);

        // layout widgets
        togglePlaceholderVisibility(null);
        initializeRecyclerView(binding.recyclerView, binding);
        initializeButtons(binding.filterButton, binding.nfcButton, binding.addButton);
        initializeClearFilterButton(binding.clearFilteredResultsButton);
        return binding.getRoot();
    }

    private void setFilterActivated(final boolean state) {
        binding.setFilterActivated(state);
        binding.filteredResultsDisclaimer.setVisibility(state ? View.VISIBLE : View.GONE);
        binding.clearFilteredResultsButton.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    public void setCallback(@NonNull UpdateActionModeCallback callback) {
        this.callback = callback;
    }

    public BoxesAdapter getAdapter() {
        return boxesAdapter;
    }

    private void initializeButtons(@NonNull FloatingActionButton filterButton, @NonNull FloatingActionButton nfcButton, @NonNull FloatingActionButton addButton) {
        fabs = new FloatingActionButton[]{nfcButton, addButton};

        filterButton.setOnClickListener(view -> {
            if(numBoxes == 0) {
                emptyListSnackbarWithAction("There are no boxes to filter");
            } else {
                if (!binding.getFilterActivated()) {
                    showCategoryPickerDialog();
                } else {
                    resetCategoryFiltering();
                }
            }
        });

        nfcButton.setOnClickListener(view -> {
            if(numBoxes == 0) {
                emptyListSnackbarWithAction("There are no boxes to scan");
            } else {
                final Intent intent = new Intent(getActivity(), NfcActivity.class);
                intent.putExtra("isWrite", false);
                startActivity(intent);
                view.setEnabled(false);
            }
        });

        addButton.setOnClickListener(view -> {
            final Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
            view.setEnabled(false);
        });
    }

    private void initializeClearFilterButton(@NonNull Button button) {
        button.setOnClickListener(v -> {
            if(binding.getFilterActivated()) {
                resetCategoryFiltering();
            }
        });
    }

    private void showCategoryPickerDialog() {
        final CategoryFilterDialog fragment = new CategoryFilterDialog();
        fragment.setDismissCallback(this);
        fragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(),fragment.getClass().getSimpleName());
    }

    private void emptyListSnackbarWithAction(@NonNull String text) {
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), text, 4000)
                .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                .setAction(R.string.create, v1 -> {
                    final Intent intent = new Intent(getActivity(), AddActivity.class);
                    startActivity(intent);
                })
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }

    /**
     * Toggles list placeholder visibility based on the value of a SharedPreferences file key.
     *
     * @param boxes Nullable list of Box objects.
     *              Pass null if toggling placeholder visibility without needing to update
     *              SharedPreferences file. Otherwise, pass list observed from the ViewModel.
     */

    private void togglePlaceholderVisibility(@Nullable List<Box> boxes) {

        final SharedPreferences preferences = Objects.requireNonNull(getActivity()).getSharedPreferences("administration", Context.MODE_PRIVATE);
        if (boxes != null) {
            final SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("areBoxes", boxes.size() > 0);
            editor.apply();
        }

        final View[] placeholders = new View[]{binding.placeholderImage, binding.placeholderText};
        for (View v : placeholders) v.setVisibility(preferences.getBoolean("areBoxes", true) ? View.GONE : View.VISIBLE);
    }

    private void initializeRecyclerView(@NonNull RecyclerView
                                                recyclerView, @NonNull FragmentPageBoxesBinding binding) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(getContext(), recyclerView, 1);
        boxesAdapter = new BoxesAdapter(Objects.requireNonNull(getActivity()));
        boxesAdapter.setCallback(callback);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(boxesAdapter);
        boxesLD.observe(this, boxes -> {
            if (boxes != null) {
                this.boxes = boxes;
                numBoxes = boxes.size();
                boxesAdapter.setBoxes(boxes);
            } else {
                this.boxes = new ArrayList<>();
                numBoxes = 0;
                boxesAdapter.setBoxes(new ArrayList<>());
            }
            togglePlaceholderVisibility(boxes != null ? boxes : new ArrayList<>());
            recyclerView.setAdapter(boxesAdapter);
        });
    }

    public void onQueryTextChange(String newText) {
        boxesAdapter.getFilter().filter(newText);
    }

    private void resetCategoryFiltering() {
        boxesAdapter.setBoxes(boxes);
        recyclerView.setAdapter(boxesAdapter);
        setFilterActivated(false);
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getString(R.string.filters_cleared), 4000)
                .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }

    @Override
    public void onDismiss(@NonNull final List<String> selectedCategories) {
        if(selectedCategories.size() > 0) {
            if (boxes != null) {
                final List<Box> filtered = new ArrayList<>();
                for (String s : selectedCategories) {
                    for (Box box : boxes) {
                        if (box.getCategories().contains(s) && !filtered.contains(box)) {
                            filtered.add(box);
                        }
                    }
                }
                if (filtered.size() > 0) {
                    boxesAdapter.setBoxes(filtered);
                    recyclerView.setAdapter(boxesAdapter);
                }
                setFilterActivated(true);
            }
        }
    }

}
