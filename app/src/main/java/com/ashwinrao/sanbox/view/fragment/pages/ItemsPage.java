package com.ashwinrao.sanbox.view.fragment.pages;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.sanbox.Sanbox;
import com.ashwinrao.sanbox.data.model.Item;
import com.ashwinrao.sanbox.databinding.FragmentPageItemsBinding;
import com.ashwinrao.sanbox.util.callback.UpdateActionModeCallback;
import com.ashwinrao.sanbox.view.adapter.ItemDisplayAdapter;
import com.ashwinrao.sanbox.viewmodel.BoxViewModel;
import com.ashwinrao.sanbox.viewmodel.ItemViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.sanbox.util.Decorations.addItemDecoration;

public class ItemsPage extends Fragment {

    private int numBoxes;
    private ItemDisplayAdapter adapter;
    private LiveData<List<Item>> itemsLD;
    private FragmentPageItemsBinding binding;
    private UpdateActionModeCallback callback;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Sanbox) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ItemViewModel itemViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), factory).get(ItemViewModel.class);
        itemsLD = itemViewModel.getAllItemsFromDatabase();
        new ViewModelProvider(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class).getBoxes().observe(getActivity(), boxes -> numBoxes = boxes != null ? boxes.size() : 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.setFilterActivated(false);
        if(adapter != null) {
            adapter.initializeFilter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageItemsBinding.inflate(inflater);

        // binding vars
        binding.setFilterActivated(false);

        // layout widgets
//        initializeButtons(binding.unpackButton, binding.filterButton, binding.packButton);
        initializeRecyclerView(binding.recyclerView);
        return binding.getRoot();
    }

    public void setCallback(@NonNull UpdateActionModeCallback callback) {
        this.callback = callback;
    }

    public ItemDisplayAdapter getAdapter() {
        return adapter;
    }

//    private void initializeButtons(@NonNull FloatingActionButton unpackButton, @NonNull FloatingActionButton filterButton, @NonNull FloatingActionButton packButton) {
//        unpackButton.setOnClickListener(view -> {
//            if(numBoxes == 0) {
//                Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "There are no boxes to unpack", 4000)
//                        .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
//                        .setAction(R.string.create, v1 -> {
//                            final Intent intent = new Intent(getActivity(), AddActivity.class);
//                            startActivity(intent);
//                        })
//                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
//                        .show();
//            } else {
//                if (adapter.getSelected() == null) {
//                    Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "Select one or more items to unpack", 4000)
//                            .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
//                            .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
//                            .show();
//                }
//            }
//        });
//
//        filterButton.setOnClickListener(view -> {
//            if(numBoxes == 0) {
//                Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "There are no items to filter", 4000)
//                        .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
//                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
//                        .show();
//            } else {
//                binding.setFilterActivated(!binding.getFilterActivated());
//                if (binding.getFilterActivated()) {
//                    // TODO
//                }
//            }
//        });
//
//        packButton.setOnClickListener(v -> {
//            if(numBoxes == 0) {
//                Snackbar.make(Objects.requireNonNull(getActivity()).findViewById(android.R.id.content), "There are no boxes to pack", 4000)
//                        .setBackgroundTint(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorAccent))
//                        .setAction(R.string.create, v1 -> {
//                            final Intent intent = new Intent(getActivity(), AddActivity.class);
//                            startActivity(intent);
//                        })
//                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_SLIDE)
//                        .show();
//            }
//        });
//    }

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

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(null);
        addItemDecoration(getContext(), recyclerView, 1);
        adapter = new ItemDisplayAdapter(Objects.requireNonNull(getActivity()), false);
        adapter.setActionModeCallback(callback);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);
        itemsLD.observe(this, items -> {
            adapter.submitList(items);
            adapter.setItemsForFiltering(items);
            togglePlaceholderVisibility(items);
        });
    }

    public void onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
    }
}
