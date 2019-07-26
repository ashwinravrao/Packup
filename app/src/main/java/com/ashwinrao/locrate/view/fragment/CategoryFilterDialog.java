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
import com.ashwinrao.locrate.util.callback.DialogDismissedCallback;
import com.ashwinrao.locrate.view.adapter.CategoriesAdapter;
import com.ashwinrao.locrate.viewmodel.BoxViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class CategoryFilterDialog extends DialogFragment {

    private DialogDismissedCallback callback;
    private CategoriesAdapter adapter;
    private boolean applyButtonClicked;
    private LiveData<List<String>> categoriesLD;
    private List<String> categories = new ArrayList<>();
    private FragmentCategoryFilterDialogBinding binding;
    private List<String> checkedCategories = new ArrayList<>();

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    ViewModelProvider.Factory factory;

    public void setDialogDismissedCallback(@NonNull DialogDismissedCallback callback) {
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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_category_filter_dialog, container, false);

        // binding vars
        binding.setMinCategoriesSelected(false);

        // layout widgets
        initializeFilterButton(binding.applyButton);
        initializeRecyclerView(binding.categoryRecyclerView);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoriesLD.removeObservers(this);
    }

    private void initializeRecyclerView(@NonNull final RecyclerView recyclerView) {
        adapter = new CategoriesAdapter(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        categoriesLD.observe(this, categories -> {
            if(categories != null) {
                this.categories = categories;
                adapter.setCategories(categories);
            }
            recyclerView.setAdapter(adapter);
        });

        adapter.getChecked().observe(this, strings -> {
            if(strings != null) {
                checkedCategories = strings;
                binding.setMinCategoriesSelected(strings.size() > 0);
            }
        });
    }

    private void initializeFilterButton(@NonNull Button button) {
        button.setOnClickListener(v -> {
            applyButtonClicked = true;
            dismiss();
        });
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(applyButtonClicked) {
            callback.onDialogDismissed(checkedCategories);
        }
    }
}
