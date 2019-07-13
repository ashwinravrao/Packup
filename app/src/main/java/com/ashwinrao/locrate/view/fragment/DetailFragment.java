package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.FragmentDetailBinding;
import com.ashwinrao.locrate.view.ConfirmationDialog;
import com.ashwinrao.locrate.view.adapter.ItemsAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;
import com.ashwinrao.locrate.viewmodel.ItemViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;


public class DetailFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private Box box;
    private ItemsAdapter itemsAdapter;
    private BoxViewModel boxViewModel;
    private LiveData<Box> boxLD;
    private LiveData<List<Item>> itemsLD;

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
        boxViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        final ItemViewModel itemViewModel = ViewModelProviders.of(getActivity(), factory).get(ItemViewModel.class);

        final Bundle extras = getActivity().getIntent().getExtras();
        if(extras != null) {
            final int boxId = extras.getInt("ID", 0);
            boxLD = boxViewModel.getBoxByID(boxId);
            itemsLD = itemViewModel.getItemsFromBox(boxId);
        } else {
            finishActivity();
        }
    }

    private void finishActivity() {
        Objects.requireNonNull(getActivity()).finish();
        getActivity().overridePendingTransition(R.anim.slide_out_to_right, R.anim.stay_still);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater);

        // layout widgets
        initializeToolbar(binding.toolbar);
        initializeRecyclerView(binding, binding.recyclerView);
        initializeButtons(binding.editButton, binding.deleteButton);
        return binding.getRoot();
    }

    private void initializeButtons(@NonNull FloatingActionButton editButton, @NonNull FloatingActionButton deleteButton) {
        editButton.setOnClickListener(view -> {

        });

        deleteButton.setOnClickListener(view -> showDeleteConfirmationDialog());
    }

    private void initializeToolbar(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.menu_toolbar_list);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> finishActivity());
    }

    private void initializeRecyclerView(@NonNull FragmentDetailBinding binding, @NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(getContext(), recyclerView, 1);
        itemsAdapter = new ItemsAdapter(Objects.requireNonNull(getActivity()), true, false);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(itemsAdapter);
        boxLD.observe(this, box -> {
            this.box = box;
            binding.setBox(box);
        });
        itemsLD.observe(this, items -> {
            itemsAdapter.setItems(items);
            binding.setNumItems(items.size());
            recyclerView.setAdapter(itemsAdapter);
        });
    }

    private void showDeleteConfirmationDialog() {
        ConfirmationDialog.make(Objects.requireNonNull(getContext()),
                new String[]{
                        getString(R.string.dialog_delete_existing_box_title),
                        getString(R.string.dialog_delete_existing_box_message),
                        getString(R.string.delete),
                        getString(R.string.no)},
                false,
                new int[]{
                        ContextCompat.getColor(getContext(), R.color.colorAccent),
                        ContextCompat.getColor(getContext(), android.R.color.holo_red_dark)},
                dialogInterface -> {
                    boxViewModel.delete(box);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    return null;
                }, dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.toolbar_search) {
            return true;
        }
        return true;
    }
}
