package com.ashwinrao.locrate.view.pages;

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
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentPageItemsBinding;
import com.ashwinrao.locrate.view.adapter.ItemsAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class ItemsPage extends Fragment {

    private ItemsAdapter itemsAdapter;
    private LiveData<List<String>> allContents;
    private FragmentPageItemsBinding binding;

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
        final BoxViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        allContents = viewModel.getAllContents();
    }

    @Override
    public void onResume() {
        super.onResume();

        // possible crashes mirroring BoxesPage issue
        binding.setFilterActivated(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageItemsBinding.inflate(inflater);

        // binding vars
        binding.setFilters(Objects.requireNonNull(getActivity()).getResources().getString(R.string.all));
        binding.setFilterActivated(false);


        // layout widgets
        initializeButtons(binding.filterButton, binding.packButton);
//        initializeRecyclerView(binding.recyclerView, binding);
        return binding.getRoot();
    }

    private void initializeButtons(@NonNull FloatingActionButton filterButton, @NonNull FloatingActionButton packButton) {
        filterButton.setOnClickListener(view -> {
            binding.setFilterActivated(!binding.getFilterActivated());
            if(binding.getFilterActivated()) {
                // TODO add method body
            }
        });

        packButton.setOnClickListener(v -> {
            // TODO add method body
        });
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView, @NonNull FragmentPageItemsBinding binding) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        addItemDecoration(getContext(), recyclerView, 1);
        itemsAdapter = new ItemsAdapter(Objects.requireNonNull(getActivity()));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(itemsAdapter);
        allContents.observe(this, strings -> {
            if(strings != null) {
//                itemsAdapter.setItems(strings);
                binding.setFilteredSize(String.format(getString(R.string.filtered_size), strings.size()));
            } else {
                itemsAdapter.setItems(new ArrayList<>());
            }
            recyclerView.setAdapter(itemsAdapter);
        });
    }

    public void onQueryTextChange(String newText) {
        itemsAdapter.getFilter().filter(newText);
    }
}
