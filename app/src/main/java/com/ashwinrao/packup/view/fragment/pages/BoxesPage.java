package com.ashwinrao.packup.view.fragment.pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.packup.Packup;
import com.ashwinrao.packup.databinding.FragmentPageBoxesBinding;
import com.ashwinrao.packup.R;
import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.util.callback.DialogDismissedCallback;
import com.ashwinrao.packup.util.callback.EmptySearchResultsCallback;
import com.ashwinrao.packup.util.callback.UpdateActionModeCallback;
import com.ashwinrao.packup.view.activity.AddActivity;
import com.ashwinrao.packup.view.activity.NfcActivity;
import com.ashwinrao.packup.view.adapter.BoxesAdapter;
import com.ashwinrao.packup.view.fragment.CategoryFilterDialog;
import com.ashwinrao.packup.viewmodel.BoxViewModel;
import com.ashwinrao.packup.viewmodel.CategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.packup.util.Decorations.addItemDecoration;

public class BoxesPage extends Fragment implements DialogDismissedCallback, EmptySearchResultsCallback {

    private RecyclerView recyclerView;
    private Parcelable recyclerViewState;

    private BoxViewModel boxViewModel;
    private CategoryViewModel categoryViewModel;

    private FragmentPageBoxesBinding binding;
    private UpdateActionModeCallback callback;

    private final String RECYCLER_VIEW_STATE_KEY = "recycler_view_state";
    private final Integer SNACKBAR_DURATION = 4000;

    private View[] emptySearchPlaceholders;

    @Inject
    ViewModelProvider.Factory factory;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Packup) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boxViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        categoryViewModel = new ViewModelProvider(getActivity(), factory).get(CategoryViewModel.class);
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getBundle(RECYCLER_VIEW_STATE_KEY);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager != null) recyclerViewState = layoutManager.onSaveInstanceState();
        outState.putParcelable(RECYCLER_VIEW_STATE_KEY, recyclerViewState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // re-enable fabs due to accidental presses
        binding.nfcButton.setEnabled(true);
        binding.addButton.setEnabled(true);

        // clear filters with stale data
        setFilterActivated(false);

        // restore list on config change
        if (recyclerViewState != null) {
            if (recyclerView.getLayoutManager() != null) {
                recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentPageBoxesBinding.inflate(inflater);

        // append views with transition_name attributes to the map
        final Pair[] viewsToTransition = new Pair[]{
                Pair.create(binding.addButton, getString(R.string.add_or_edit_button_transition_name)),
                Pair.create(binding.categoriesButton, getString(R.string.categories_or_delete_button_transition_name))
        };

        emptySearchPlaceholders = new View[]{
                binding.emptySearchPlaceholder,
                binding.emptySearchPlaceholderText
        };

        // init filter state
        setFilterActivated(false);

        // layout widgets
        togglePlaceholderVisibility(null);
        setupRecyclerView(binding.recyclerView, viewsToTransition);
        setupButtons(binding.categoriesButton, binding.nfcButton, binding.addButton, binding.clearFiltersButton);
        return binding.getRoot();
    }


    private void setFilterActivated(final boolean state) {

        binding.setFilterActivated(state);
        binding.showingFilteredResultsCaption.setVisibility(state ? View.VISIBLE : View.GONE);
        binding.clearFiltersButton.setVisibility(state ? View.VISIBLE : View.GONE);
    }

    public void setCallback(@NonNull UpdateActionModeCallback callback) {
        this.callback = callback;
    }

    public BoxesAdapter getBoxesAdapter() { return (BoxesAdapter) recyclerView.getAdapter(); }

    private void setupButtons(@NonNull FloatingActionButton filterButton,
                              @NonNull FloatingActionButton nfcButton,
                              @NonNull FloatingActionButton addButton,
                              @NonNull Button clearFiltersButton) {

        filterButton.setOnClickListener(view -> {
            if (boxViewModel.getCachedBoxes().size() == 0) {
                showEmptyListSnackbar("There are no boxes to filter", true);
            } else {
                if(categoryViewModel.getCachedBoxCategories().size() > 0) {
                    if (!binding.getFilterActivated()) {
                        showCategoryPickerDialog();
                    } else {
                        resetCategoryFiltering();
                    }
                } else {
                    showEmptyListSnackbar("There are no categories to filter on", false);
                }
            }
        });

        nfcButton.setOnClickListener(view -> {
            if (boxViewModel.getCachedBoxes().size() == 0) {
                showEmptyListSnackbar("There are no boxes to scan", true);
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

        clearFiltersButton.setOnClickListener(v -> {
            if (binding.getFilterActivated()) {
                resetCategoryFiltering();
            }
        });
    }

    private void showCategoryPickerDialog() {
        final CategoryFilterDialog fragment = new CategoryFilterDialog();
        fragment.setDialogDismissedCallback(this);
        fragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), fragment.getClass().getSimpleName());
    }

    private void showEmptyListSnackbar(@NonNull String text, @NonNull Boolean showDefaultAction) {
        final Snackbar snackbar = Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), text, SNACKBAR_DURATION)
                .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE);
        if(showDefaultAction) {
            snackbar.setAction(R.string.create, v1 -> {
                final Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivity(intent);
            });
        }
        snackbar.show();
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
        for (View v : placeholders)
            v.setVisibility(preferences.getBoolean("areBoxes", true) ? View.GONE : View.VISIBLE);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull Pair[] viewsToTransition) {
        this.recyclerView = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(null);
        addItemDecoration(getContext(), recyclerView, 1, null);
        final BoxesAdapter boxesAdapter =
                new BoxesAdapter(Objects.requireNonNull(getActivity()));
        boxesAdapter.setHasStableIds(true);
        boxesAdapter.setUpdateActionModeCallback(callback);
        boxesAdapter.setEmptySearchResultsCallback(this);
        boxesAdapter.setViewsToTransition(viewsToTransition);
        recyclerView.setAdapter(boxesAdapter);

        boxViewModel.getBoxes().observe(this, boxes -> {
            boxesAdapter.submitList(boxes);
            boxesAdapter.setBoxesForFiltering(boxes);
            boxViewModel.setCachedBoxes(boxes);
            categoryViewModel.setCachedBoxCategories(boxes);
            togglePlaceholderVisibility(boxes);
        });
    }

    public void onQueryTextChange(String newText) {
        getBoxesAdapter().getFilter().filter(newText);
    }

    private void resetCategoryFiltering() {
        getBoxesAdapter().submitList(boxViewModel.getCachedBoxes());
        setFilterActivated(false);
        Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getString(R.string.filters_cleared), SNACKBAR_DURATION)
                .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                .show();
    }

    @Override
    public void onDialogDismissed(@NonNull final List<String> selectedCategories) {

        if (selectedCategories.size() > 0) {
            if (boxViewModel.getCachedBoxes().size() > 0) {
                final List<Box> filtered = new ArrayList<>();
                for (String s : selectedCategories) {
                    for (Box box : boxViewModel.getCachedBoxes()) {
                        if (box.getCategories().contains(s) && !filtered.contains(box)) {
                            filtered.add(box);
                        }
                    }
                }

                if (filtered.size() > 0 && !filtered.equals(boxViewModel.getCachedBoxes())) {
                    getBoxesAdapter().submitList(filtered);
                    setFilterActivated(true);
                }

                if (filtered.equals(boxViewModel.getCachedBoxes())) {
                    Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), getString(R.string.showing_all), SNACKBAR_DURATION)
                            .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .show();
                }
            }
        }
    }

    /**
     * Handles visibility of the "no search results" placeholder image and text. An instance of
     * this callback is passed to the RecyclerView Adapter responsible for displaying boxes.
     *
     * @param numResults zero or positive. Zero indicates no results were found, and a placeholder
     *                   should be shown to the user. Positive indicates the opposite.
     */

    @Override
    public void handleEmptyResults(int numResults) {
        for (View view : emptySearchPlaceholders) {
            view.setVisibility(numResults > 0 ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
