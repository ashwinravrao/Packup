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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ViewholderItemDisplayBinding;
import com.ashwinrao.locrate.databinding.ViewholderItemPackingBinding;
import com.ashwinrao.locrate.util.HashtagDetection;
import com.ashwinrao.locrate.util.ItemPropertiesFilter;
import com.ashwinrao.locrate.util.callback.DiffUtilCallback;
import com.ashwinrao.locrate.util.callback.SingleItemDeleteCallback;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private Filter filter;
    private Context context;
    private List<Item> items;
    private List<Item> itemsCopy;
    private List<String> categories;
    private Boolean isShownWithBoxContext;
    private Boolean isInPackingMode;
    private MutableLiveData<Item> editedItem = new MutableLiveData<>();

    private final Boolean[] matchFound = {false};
    private final String[] matchStrings = {null, null};

    // Action Mode
    private UpdateActionModeCallback updateActionModeCallback;
    private List<Object> selected;

    // Single Item Delete Callback
    private SingleItemDeleteCallback singleItemDeleteCallback;

    public ItemsAdapter(@NonNull Context context, @NonNull Boolean isShownWithBoxContext, @NonNull Boolean isInPackingMode) {
        this.context = context;
        this.isShownWithBoxContext = isShownWithBoxContext;
        this.isInPackingMode = isInPackingMode;
        initializeFilter();
    }

    public void setCategories(@NonNull List<String> categories) {
        this.categories = categories;
    }

    /**
     * For use by external classes.
     *
     * @return edited item to be persisted by a LifecycleOwner.
     */

    public LiveData<Item> getEditedItem() {
        return editedItem;
    }

    public void initializeFilter() {
        this.filter = createFilter();
    }

    public void setItems(@NonNull List<Item> items) {
        this.items = items;
        this.itemsCopy = new ArrayList<>(items);
    }

    public void setActionModeCallback(@NonNull UpdateActionModeCallback callback) {
        this.updateActionModeCallback = callback;
    }

    public void setSingleItemDeleteCallback(@NonNull SingleItemDeleteCallback callback) {
        this.singleItemDeleteCallback = callback;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(!isInPackingMode) {
            final ViewholderItemDisplayBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item_display, parent, false);
            return new ItemViewHolderDisplay(binding);
        } else {
            final ViewholderItemPackingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item_packing, parent, false);
            return new ItemViewHolderPacking(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Item item = items.get(position);
        if(!isInPackingMode) {
            final ItemViewHolderDisplay viewHolder = (ItemViewHolderDisplay) holder;
            viewHolder.binding.setItem(item);
            viewHolder.binding.setSelected(false);
            item.setIsShownWithBoxContext(isShownWithBoxContext);
            loadImage(context, item, viewHolder.binding.itemImage);
        } else {
            loadImage(context, item, ((ItemViewHolderPacking) holder).binding.itemImage);
        }
    }

    private void loadImage(@NonNull Context context, @NonNull final Item item, @NonNull ImageView imageView) {
        final String filePath = item.getFilePath();
        Glide.with(context)
                .load(new File(filePath))
                .thumbnail(0.01f)  // down-sample to 1% of original image resolution for thumbnail
                .centerCrop()
                .into(imageView);
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

    class ItemViewHolderPacking extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewholderItemPackingBinding binding;

        ItemViewHolderPacking(@NonNull ViewholderItemPackingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.removeButton.setOnClickListener(this);
            this.binding.itemNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    items.get(getAdapterPosition()).setName(s.toString().length() > 0 ? s.toString() : null);
                    editedItem.setValue(items.get(getAdapterPosition()));
                }
            });
            this.binding.estimatedValueEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    items.get(getAdapterPosition()).setEstimatedValue(Double.valueOf(s.toString().length() > 0 ? s.toString() : ""));
                    editedItem.setValue(items.get(getAdapterPosition()));
                }
            });
            this.binding.categoryField.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    HashtagDetection.detect(s, categories, matchFound, matchStrings);
                }

                @Override
                public void afterTextChanged(Editable s) {
                    tagToChip(s, matchFound, matchStrings, binding.categoryChipGroup);
                    editedItem.setValue(items.get(getAdapterPosition()));
                }
            });
        }

        @Override
        public void onClick(View v) {
            singleItemDeleteCallback.deleteItem(items.get(getAdapterPosition()), getAdapterPosition());
        }

        private void tagToChip(@NonNull Editable s, @NonNull Boolean[] matchFound, @NonNull String[] matchStrings, @NonNull ChipGroup group) {
            if(matchFound[0]) {
                s.delete(s.length() - matchStrings[0].length(), s.length());
                addNewCategoryChip(group);
            }
        }

        private void addNewCategoryChip(@NonNull ChipGroup group) {
            final Chip chip = new Chip(group.getContext());
            chip.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            chip.setChipBackgroundColorResource(R.color.colorAccent);
            chip.setText(categories.get(categories.size()-1));
            chip.setCloseIconVisible(true);
            chip.setCloseIconTintResource(android.R.color.white);
            chip.setOnCloseIconClickListener(v -> {
                group.removeView(v);
                categories.remove(categories.size()-1);
            });
            group.addView(chip);
        }
    }

    class ItemViewHolderDisplay extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ViewholderItemDisplayBinding binding;

        ItemViewHolderDisplay(@NonNull ViewholderItemDisplayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            this.binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(selected != null && updateActionModeCallback != null) {
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
