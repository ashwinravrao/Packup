package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ViewholderItemBinding;
import com.ashwinrao.locrate.util.ItemPropertiesFilter;
import com.ashwinrao.locrate.util.callback.DiffUtilCallback;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> implements Filterable {

    private Filter filter;
    private Context context;
    private List<Item> items;
    private List<Item> itemsCopy;
    private Boolean isShownWithBoxContext;
    private Boolean isInPackingMode;
    private MutableLiveData<Item> renamedItem = new MutableLiveData<>();

    // Action Mode
    private UpdateActionModeCallback updateActionModeCallback;
    private List<Object> selected;


    public ItemsAdapter(@NonNull Context context, @NonNull Boolean isShownWithBoxContext, @NonNull Boolean isInPackingMode) {
        this.context = context;
        this.isShownWithBoxContext = isShownWithBoxContext;
        this.isInPackingMode = isInPackingMode;
        initializeFilter();
    }

    public LiveData<Item> getRenamedItem() {
        return renamedItem;
    }

    public void initializeFilter() {
        this.filter = createFilter();
    }

    public void setItems(@NonNull List<Item> items) {
        this.items = items;
        this.itemsCopy = new ArrayList<>(items);
    }

    public void setCallback(@NonNull UpdateActionModeCallback callback) {
        this.updateActionModeCallback = callback;
    }

    public List<Object> getSelected() {
        return this.selected;
    }

    public void clearSelected() {
        this.selected = null;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Item item = items.get(position);
        holder.binding.setItem(item);
        holder.binding.setSelected(false);
        item.setIsShownWithBoxContext(isShownWithBoxContext);
        if(isInPackingMode && item.getName().length() > 0) holder.binding.itemNameEditText.setText(item.getName());
        holder.binding.itemNameEditText.setVisibility(isInPackingMode ? View.VISIBLE : View.GONE);
        holder.binding.itemNameTextView.setVisibility(!isInPackingMode ? View.VISIBLE : View.GONE);

        watchText(holder.binding.itemNameEditText, item);

        final String path = item.getFilePath();
        Glide.with(context)
                .load(new File(path))
                .thumbnail(0.01f)  // down-sample to 1% of original image resolution for thumbnail
                .centerCrop()
                .into(holder.binding.itemImage);
    }

    private void watchText(@NonNull EditText editText, @NonNull Item item) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                item.setName(s.toString().length() > 0 ? s.toString() : null);
                renamedItem.setValue(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Filter getFilter() {
        return this.filter;
    }

    private Filter createFilter() {
        return new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                List<Item> filtered;
                ItemPropertiesFilter pf = new ItemPropertiesFilter(itemsCopy);
                filtered = pf.filter(constraint, true);

                Filter.FilterResults results = new Filter.FilterResults();
                results.values = filtered;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                final DiffUtilCallback diffUtil = new DiffUtilCallback(new ArrayList<>(items), (List) results.values);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtil, true);

                items.clear();
                items.addAll((List) results.values);
                result.dispatchUpdatesTo(ItemsAdapter.this);
            }
        };
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ViewholderItemBinding binding;

        ItemViewHolder(@NonNull ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            this.binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(updateActionModeCallback != null) {
                final Item item = items.get(getAdapterPosition());
                if(selected.contains(item)) {
                    selected.remove(item);
                } else {
                    selected.add(item);
                }
                binding.setSelected(!binding.getSelected());
                updateActionModeCallback.update(selected, getObjectTypeString());
            }
        }

        private String getObjectTypeString() {
            return (selected.size() < 1 || selected.size() > 1) ? "items" : "item";
        }

        @Override
        public boolean onLongClick(View v) {
            if(selected == null) {
                selected = new ArrayList<>();
                if (updateActionModeCallback != null) {
                    selected.add(items.get(getAdapterPosition()));
                    if(updateActionModeCallback.update(selected, getObjectTypeString())) {
                        binding.setSelected(!binding.getSelected());
                    } else {
                        selected = null;
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

}
