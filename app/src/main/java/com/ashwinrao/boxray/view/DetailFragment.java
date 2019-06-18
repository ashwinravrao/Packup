package com.ashwinrao.boxray.view;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.FragmentDetailBinding;
import com.ashwinrao.boxray.view.adapter.ThumbnailAdapter;
import com.ashwinrao.boxray.viewmodel.BoxViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
        if(getArguments() != null) {
            boxId = getArguments().getInt("ID", 0);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentDetailBinding binding = FragmentDetailBinding.inflate(inflater);
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

    private void setupRecyclerView(@NonNull FragmentDetailBinding binding, @NonNull RecyclerView rv) {
        this.recyclerView = rv;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(getContext(), recyclerView, 2);
        adapter = new ThumbnailAdapter(getContext(), dpToPx(Objects.requireNonNull(getContext()), 150f), dpToPx(getContext(), 150f));
        recyclerView.setAdapter(adapter);
        viewModel.getBoxByID(boxId).observe(this, box -> {
            this.box = box;
            binding.setBox(box);
            adapter.setPaths(box.getContents());
            recyclerView.setAdapter(adapter);
        });
    }

    private void createDeleteConfirmationDialog(@NonNull Context context) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.dialog_delete_existing_box_title))
                .setMessage(getString(R.string.dialog_delete_existing_box_message))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.delete), (dialog1, which) -> {
                    viewModel.delete(box);
                    Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialog12, which) -> dialog12.cancel())
                .create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark));
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            createDeleteConfirmationDialog(Objects.requireNonNull(getContext()));
            return true;
        }
        return true;
    }
}
