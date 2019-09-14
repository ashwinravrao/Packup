package com.ashwinrao.packup.view.fragment.pages;

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

import com.ashwinrao.packup.Packup;
import com.ashwinrao.packup.data.model.Item;
import com.ashwinrao.packup.databinding.FragmentPageItemsBinding;
import com.ashwinrao.packup.util.callback.EmptySearchResultsCallback;
import com.ashwinrao.packup.util.callback.UpdateActionModeCallback;
import com.ashwinrao.packup.view.adapter.ItemDisplayAdapter;
import com.ashwinrao.packup.viewmodel.BoxViewModel;
import com.ashwinrao.packup.viewmodel.ItemViewModel;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.packup.util.Decorations.addItemDecoration;

public class ItemsPage extends Fragment implements EmptySearchResultsCallback {

    private ItemDisplayAdapter adapter;
    private LiveData<List<Item>> itemsLD;
    private FragmentPageItemsBinding binding;
    private UpdateActionModeCallback callback;

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
        final ItemViewModel itemViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), factory).get(ItemViewModel.class);
        itemsLD = itemViewModel.getAllItemsFromDatabase();
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

        emptySearchPlaceholders = new View[]{
            binding.emptySearchPlaceholder,
            binding.emptySearchPlaceholderText
        };

        // binding vars
        binding.setFilterActivated(false);

        initializeRecyclerView(binding.recyclerView);
        return binding.getRoot();
    }

    public void setCallback(@NonNull UpdateActionModeCallback callback) {
        this.callback = callback;
    }

    public ItemDisplayAdapter getAdapter() {
        return adapter;
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

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setItemAnimator(null);
        addItemDecoration(getContext(), recyclerView, 1, null);
        adapter = new ItemDisplayAdapter(Objects.requireNonNull(getActivity()), false);
        adapter.setActionModeCallback(callback);
        adapter.setEmptySearchResultsCallback(this);
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

    /**
     * Handles visibility of the "no search results" placeholder image and text. An instance of
     * this callback is passed to the RecyclerView Adapter responsible for displaying items.
     *
     * @param numResults zero or positive. Zero indicates no results were found, and a placeholder
     *                   should be shown to the user. Positive indicates the opposite.
     */

    @Override
    public void handleEmptyResults(@NonNull Integer numResults) {
        for (View view : emptySearchPlaceholders) {
            view.setVisibility(numResults > 0 ? View.INVISIBLE : View.VISIBLE);
        }
    }
}
