package com.ashwinrao.locrate.view.pages;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.databinding.FragmentListPageBinding;
import com.ashwinrao.locrate.view.adapter.ThumbnailAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;

import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;
import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class ListItemsPageFragment extends Fragment {

    private BoxViewModel viewModel;

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
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentListPageBinding binding = FragmentListPageBinding.inflate(inflater);
        setupRecyclerView(binding.recyclerView);
        return binding.getRoot();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        addItemDecoration(getContext(), recyclerView, 2);
        final ThumbnailAdapter adapter = new ThumbnailAdapter(getContext(), dpToPx(Objects.requireNonNull(getContext()), 150f), dpToPx(getContext(), 150f));
        recyclerView.setAdapter(adapter);

        new Handler().post(() -> {
            adapter.setPaths(viewModel.getAllItemPaths());
            recyclerView.setAdapter(adapter);
        });
    }
}
