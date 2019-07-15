package com.ashwinrao.locrate.view.fragment.pages;

import android.content.Context;
import android.content.Intent;
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
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.FragmentPageBoxesBinding;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.activity.NfcActivity;
import com.ashwinrao.locrate.view.adapter.BoxesAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class BoxesPage extends Fragment {

    private BoxesAdapter boxesAdapter;
    private LiveData<List<Box>> boxesLD;
    private FragmentPageBoxesBinding binding;
    private FloatingActionButton[] fabs;
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
        final BoxViewModel boxViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        boxesLD = boxViewModel.getBoxes();
    }

    @Override
    public void onResume() {
        super.onResume();
        for (FloatingActionButton fab : fabs) fab.setEnabled(true);
        binding.setFilterActivated(false);
        if(boxesAdapter != null) {
            boxesAdapter.initializeFilter();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageBoxesBinding.inflate(inflater);

        // binding vars
        binding.setFilterActivated(false);

        // layout widgets
        initializeRecyclerView(binding.recyclerView, binding);
        initializeButtons(binding.filterButton, binding.nfcButton, binding.addButton);
        return binding.getRoot();
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
            binding.setFilterActivated(!binding.getFilterActivated());
            if (binding.getFilterActivated()) {
                // TODO add method body
            }
        });

        nfcButton.setOnClickListener(view -> {
            final Intent intent = new Intent(getActivity(), NfcActivity.class);
            intent.putExtra("isWrite", false);
            startActivity(intent);
            view.setEnabled(false);
        });

        addButton.setOnClickListener(view -> {
            final Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
            view.setEnabled(false);
        });
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView, @NonNull FragmentPageBoxesBinding binding) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(getContext(), recyclerView, 1);
        boxesAdapter = new BoxesAdapter(Objects.requireNonNull(getActivity()));
        boxesAdapter.setCallback(callback);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(boxesAdapter);
        boxesLD.observe(this, boxes -> {
            if (boxes != null) {
                boxesAdapter.setBoxes(boxes);
            } else {
                boxesAdapter.setBoxes(new ArrayList<>());
            }
            recyclerView.setAdapter(boxesAdapter);
        });
    }

    public void onQueryTextChange(String newText) {
        boxesAdapter.getFilter().filter(newText);
    }
}
