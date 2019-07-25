package com.ashwinrao.locrate.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.Locrate;
import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.databinding.FragmentCategoryFilterDialogBinding;
import com.ashwinrao.locrate.util.callback.DismissCallback;
import com.ashwinrao.locrate.view.adapter.CategoriesAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class CategoryFilterDialog extends DialogFragment {

    private DismissCallback callback;
    private boolean filterButtonClicked;
    private LiveData<List<String>> categoriesLD;
    private List<String> checkedCategories = new ArrayList<>();

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory factory;

    public void setDismissCallback(DismissCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onResume() {
        super.onResume();
        final WindowManager.LayoutParams params = Objects.requireNonNull(Objects.requireNonNull(getDialog()).getWindow()).getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Objects.requireNonNull(getDialog().getWindow()).setAttributes(params);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((Locrate) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final BoxViewModel boxViewModel = ViewModelProviders
                .of(Objects.requireNonNull(getActivity()), factory)
                .get(BoxViewModel.class);
        categoriesLD = boxViewModel.getAllBoxCategories();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentCategoryFilterDialogBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_filter_dialog, container, false);
        initializeFilterButton(binding.filterButton);
        initializeRecyclerView(binding.categoryRecyclerView);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoriesLD.removeObservers(this);
    }

    private void initializeRecyclerView(@NonNull final RecyclerView recyclerView) {
        final CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        categoriesLD.observe(this, list -> {
            if(list != null) {
                adapter.setCategories(list);
            }
            recyclerView.setAdapter(adapter);
        });

        adapter.getChecked().observe(this, strings -> {
            if(strings != null) checkedCategories = strings;
        });
    }

    private void initializeFilterButton(@NonNull Button button) {
        button.setOnClickListener(v -> {
            filterButtonClicked = true;
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(filterButtonClicked) callback.onDismiss(checkedCategories);
    }
}
