package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentDetailAltBinding;
import com.ashwinrao.boxray.util.ConfirmationDialog;
import com.ashwinrao.boxray.view.adapter.ThumbnailAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

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

import static com.ashwinrao.boxray.util.Decorations.addItemDecoration;
import static com.ashwinrao.boxray.util.UnitConversion.dpToPx;


public class DetailFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private Box box;
    private int boxId = 0;
    private BoxViewModel viewModel;
    private ThumbnailAdapter adapter;
    private RecyclerView recyclerView;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Boxray) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        if (getArguments() != null) {
            boxId = getArguments().getInt("ID", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailAltBinding binding = FragmentDetailAltBinding.inflate(inflater);
        setupToolbar(binding.toolbar);
        setupRecyclerView(binding, binding.recyclerView);
        return binding.getRoot();
    }

    private void setupToolbar(Toolbar toolbar) {
        toolbar.inflateMenu(R.menu.toolbar_detail);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationOnClickListener(view -> {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
        });
    }

    private void setupRecyclerView(@NonNull FragmentDetailAltBinding binding, @NonNull RecyclerView rv) {
        this.recyclerView = rv;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), dpToPx(Objects.requireNonNull(getContext()), 150f), dpToPx(getContext(), 150f));
        recyclerView.setAdapter(adapter);
        viewModel.getBoxByID(boxId).observe(this, box -> {
            this.box = box;
            binding.setBox(box);
//            binding.priority.setVisibility(box.isPriority() ? View.VISIBLE : View.GONE);
            adapter.setPaths(box.getContents());
            recyclerView.setAdapter(adapter);
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
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(getActivity(), AddActivity.class);
                intent.putExtra("ID", boxId);
                startActivity(intent);
                return true;
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
        }
        return true;
    }
}
