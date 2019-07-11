package com.ashwinrao.locrate.view.pages;

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
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.FragmentPageBoxesBinding;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.adapter.BoxesAdapter;
import com.ashwinrao.locrate.view.fragment.NFCReadFragment;
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
        binding.setBulkEditActivated(false);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPageBoxesBinding.inflate(inflater);

        // binding vars
        binding.setFilters(Objects.requireNonNull(getActivity()).getResources().getString(R.string.all));
        binding.setFilterActivated(false);
        binding.setBulkEditActivated(false);

        // layout widgets
        initializeRecyclerView(binding.recyclerView, binding);
        initializeButtons(binding.bulkEditButton, binding.filterButton, binding.nfcButton, binding.addButton);
        return binding.getRoot();
    }

    private void initializeButtons(@NonNull FloatingActionButton bulkEditButton, @NonNull FloatingActionButton filterButton, @NonNull FloatingActionButton nfcButton, @NonNull FloatingActionButton addButton) {
        fabs = new FloatingActionButton[]{nfcButton, addButton};

        bulkEditButton.setOnClickListener(view -> {
            binding.setBulkEditActivated(!binding.getBulkEditActivated());
            if(binding.getBulkEditActivated()) {
                // TODO add method body
            }
        });

        filterButton.setOnClickListener(view -> {
            binding.setFilterActivated(!binding.getFilterActivated());
            if (binding.getFilterActivated()) {
                // TODO add method body
            }
        });

        nfcButton.setOnClickListener(view -> {
            Objects.requireNonNull(getActivity())
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(R.anim.slide_up_in,
                            R.anim.stay_still,
                            R.anim.stay_still,
                            R.anim.slide_down_out)
                    .replace(R.id.fragment_container, new NFCReadFragment(), "NFCReadFragment")
                    .commit();
            view.setEnabled(false);
        });

        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            startActivity(intent);
            view.setEnabled(false);
        });
    }

    private void initializeRecyclerView(@NonNull RecyclerView recyclerView, @NonNull FragmentPageBoxesBinding binding) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(getContext(), recyclerView, 1);
        boxesAdapter = new BoxesAdapter(Objects.requireNonNull(getActivity()));
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(boxesAdapter);
        boxesLD.observe(this, boxes -> {
            if (boxes != null) {
                boxesAdapter.setBoxes(boxes);
                binding.setFilteredSize(String.format(getString(R.string.filtered_size), boxes.size()));
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
