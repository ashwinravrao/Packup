package com.ashwinrao.sanbox.view.fragment;

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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.sanbox.Sanbox;
import com.ashwinrao.sanbox.R;
import com.ashwinrao.sanbox.databinding.FragmentCategoryFilterDialogBinding;
import com.ashwinrao.sanbox.util.callback.DialogDismissedCallback;
import com.ashwinrao.sanbox.view.adapter.CategoriesAdapter;
import com.ashwinrao.sanbox.viewmodel.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class CategoryFilterDialog extends DialogFragment {

    private DialogDismissedCallback callback;
    private boolean applyButtonClicked;
    private CategoryViewModel categoryViewModel;
    private List<String> selectedCategories = new ArrayList<>();


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
        ((Sanbox) context.getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryViewModel = new ViewModelProvider(Objects.requireNonNull(getActivity()), factory).get(CategoryViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final FragmentCategoryFilterDialogBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category_filter_dialog, container, false);

        // binding vars
        binding.setMinCategoriesSelected(false);

        // layout widgets
        initializeFilterButton(binding.applyButton);
        initializeRecyclerView(binding.categoryRecyclerView, binding);
        return binding.getRoot();
    }

    private void initializeRecyclerView(@NonNull final RecyclerView recyclerView, @NonNull final FragmentCategoryFilterDialogBinding binding) {
        final CategoriesAdapter adapter = new CategoriesAdapter(getActivity());
        adapter.setCategories(categoryViewModel.getCachedBoxCategories());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        adapter.getChecked().observe(this, strings -> {
            if(strings != null) {
                selectedCategories = strings;
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
            callback.onDialogDismissed(selectedCategories);
        }
    }
}
