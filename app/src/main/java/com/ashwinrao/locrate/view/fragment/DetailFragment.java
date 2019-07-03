package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.FragmentDetailBinding;
import com.ashwinrao.locrate.view.ConfirmationDialog;
import com.ashwinrao.locrate.view.activity.AddActivity;
import com.ashwinrao.locrate.view.adapter.ThumbnailAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;
import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;


public class DetailFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private Box box;
    private String boxId;
    private BoxViewModel viewModel;
    private ThumbnailAdapter adapter;
    private RecyclerView recyclerView;

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
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        if (getArguments() != null) {
            boxId = getArguments().getString("ID", "0");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater);
        setupToolbar(binding.toolbar);
        setupRecyclerView(binding, binding.recyclerView);
        setupBottomNavigation(binding.deleteButton, binding.editButton);
        return binding.getRoot();
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.toolbar_detail);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack());
    }

    private void setupRecyclerView(@NonNull FragmentDetailBinding binding, @NonNull RecyclerView rv) {
        this.recyclerView = rv;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), dpToPx(Objects.requireNonNull(getContext()), 150f), dpToPx(getContext(), 150f));
        recyclerView.setAdapter(adapter);
        viewModel.getBoxByID(boxId).observe(this, box -> {
            this.box = box;
            binding.setBox(box);
            new Handler().post(() -> {
                adapter.setPaths(box.getContents());
                recyclerView.setAdapter(adapter);
            });
        });
    }

    private void setupBottomNavigation(@NonNull ImageView delete, @NonNull ImageView edit) {
        delete.setOnClickListener(view -> showDeleteConfirmationDialog());
        edit.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), AddActivity.class);
            intent.putExtra("ID", boxId);
            startActivity(intent);
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
                    viewModel.delete(box);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                    return null;
                }, dialogInterface -> {
                    dialogInterface.cancel();
                    return null;
                });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.archive) {
            return true;
        }
        return true;
    }
}
