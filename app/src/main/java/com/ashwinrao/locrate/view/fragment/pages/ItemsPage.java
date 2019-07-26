package com.ashwinrao.locrate.view.fragment.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.FragmentPageItemsBinding;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.adapter.ItemsAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class ItemsPage extends Fragment {

    private int numBoxes;
    private ItemsAdapter itemsAdapter;
    private LiveData<List<Item>> itemsLD;
    private FragmentPageItemsBinding binding;
    private UpdateActionModeCallback callback;

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
        itemsLD = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(ItemViewModel.class).getAllItemsFromDatabase();
        ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class).getBoxes().observe(getActivity(), boxes -> numBoxes = boxes != null ? boxes.size() : 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.setFilterActivated(false);
        if(itemsAdapter != null) {
            itemsAdapter.initializeFilter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageItemsBinding.inflate(inflater);

        // binding vars
        binding.setFilterActivated(false);

        // layout widgets
        initializeButtons(binding.unpackButton, binding.filterButton, binding.packButton);
        initializeRecyclerView(binding.recyclerView, binding);
        return binding.getRoot();
    }

    public void setCallback(@NonNull UpdateActionModeCallback callback) {
        this.callback = callback;
    }

    public ItemsAdapter getAdapter() {
        return itemsAdapter;
    }

    private void initializeButtons(@NonNull FloatingActionButton unpackButton, @NonNull FloatingActionButton filterButton, @NonNull FloatingActionButton packButton) {
        unpackButton.setOnClickListener(view -> {
            if(numBoxes == 0) {
                Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "There are no boxes to unpack", 4000)
                        .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                        .setAction(R.string.create, v1 -> {
                            final Intent intent = new Intent(getActivity(), AddActivity.class);
                            startActivity(intent);
                        })
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .show();
            } else {
                if (itemsAdapter.getSelected() == null) {
                    Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "Select one or more items to unpack", 4000)
                            .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                            .show();
                }
            }
        });

        filterButton.setOnClickListener(view -> {
            if(numBoxes == 0) {
                Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "There are no items to filter", 4000)
                        .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .show();
            } else {
                binding.setFilterActivated(!binding.getFilterActivated());
                if (binding.getFilterActivated()) {
                    // TODO
                }
            }
        });

        packButton.setOnClickListener(v -> {
            if(numBoxes == 0) {
                Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "There are no boxes to pack", 4000)
                        .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
                        .setAction(R.string.create, v1 -> {
                            final Intent intent = new Intent(getActivity(), AddActivity.class);
                            startActivity(intent);
                        })
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
                        .show();
            }
        });
    }

    private void togglePlaceholderVisibility(@Nullable List<Item> items) {
        final View[] placeholders = new View[]{binding.placeholderImage, binding.placeholderText};
        for(View v : placeholders) {
            if(items != null) {
                v.setVisibility(items.size() > 0 ? View.GONE : View.VISIBLE);
            } else {
                v.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView, @NonNull FragmentPageItemsBinding binding) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        addItemDecoration(getContext(), recyclerView, 1);
        itemsAdapter = new ItemsAdapter(Objects.requireNonNull(getActivity()), false, false);
        itemsAdapter.setCallback(callback);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(itemsAdapter);
        itemsLD.observe(this, items -> {
            if(items != null) {
                itemsAdapter.setItems(items);
            } else {
                itemsAdapter.setItems(new ArrayList<>());
            }
            togglePlaceholderVisibility(items);
            recyclerView.setAdapter(itemsAdapter);
        });
    }

    public void onQueryTextChange(String newText) {
        itemsAdapter.getFilter().filter(newText);
    }
}
