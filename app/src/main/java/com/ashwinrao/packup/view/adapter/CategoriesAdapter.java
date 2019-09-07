package com.ashwinrao.packup.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.packup.R;
import com.ashwinrao.packup.databinding.ViewholderCategoryBinding;

import java.util.ArrayList;
import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private Context context;
    private List<String> categories = new ArrayList<>();
    private List<String> checked = new ArrayList<>();
    private MutableLiveData<List<String>> checkedMLD = new MutableLiveData<>();

    public CategoriesAdapter(Context context) {
        this.context = context;
    }

    public void setCategories(@NonNull List<String> categories) {
        this.categories = categories;
    }

    public LiveData<List<String>> getChecked() {
        return checkedMLD;
    }

    private void check(@NonNull String category) {
        checked.add(category);
        checkedMLD.setValue(checked);
    }

    private void uncheck(@NonNull String category) {
        checked.remove(category);
        checkedMLD.setValue(checked);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_category, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.setCategory(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewholderCategoryBinding binding;

        ViewHolder(@NonNull ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            this.binding.checkbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if(isChecked) check(categories.get(getAdapterPosition()));
                else uncheck(categories.get(getAdapterPosition()));
            });
        }

        @Override
        public void onClick(View v) {
            binding.checkbox.setChecked(!binding.checkbox.isChecked());
        }
    }
}
