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
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.FragmentListPageBinding;
import com.ashwinrao.locrate.util.callback.OnScrollCallback;
import com.ashwinrao.locrate.view.adapter.ListAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.ashwinrao.locrate.util.Decorations.addItemDecoration;

public class ListBoxesPageFragment extends Fragment implements TabLayoutViewPage {

    private OnScrollCallback callback;
    private ListAdapter listAdapter;
    private LiveData<List<Box>> boxesLD;

    @Inject
    ViewModelProvider.Factory factory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Locrate) context.getApplicationContext()).getAppComponent().inject(this);
    }

    public void setScrollCallback(@NonNull OnScrollCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BoxViewModel viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), factory).get(BoxViewModel.class);
        boxesLD = viewModel.getBoxes();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentListPageBinding binding = FragmentListPageBinding.inflate(inflater);
        setupRecyclerView(binding.recyclerView);
        return binding.getRoot();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if(callback != null) callback.onScroll(recyclerView.getScrollX(), recyclerView.getScrollY());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        addItemDecoration(getContext(), recyclerView, 1);
        listAdapter = new ListAdapter(Objects.requireNonNull(getActivity()));
        recyclerView.setAdapter(listAdapter);
        boxesLD.observe(this, boxes -> {
            if(boxes != null) {
                listAdapter.setBoxes(boxes);
            } else {
                listAdapter.setBoxes(new ArrayList<>());
            }
            recyclerView.setAdapter(listAdapter);
        });
    }

    @Override
    public String getTitle() {
        return "Boxes";
    }
}
