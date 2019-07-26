package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.databinding.ViewholderBoxBinding;
import com.ashwinrao.locrate.util.BoxPropertiesFilter;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.ashwinrao.locrate.view.activity.DetailActivity;
import com.ashwinrao.locrate.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class BoxesAdapter extends ListAdapter<Box, BoxesAdapter.BoxViewHolder> implements Filterable {

    private Filter filter;
    private Context context;
    private List<Box> boxesForFiltering;

    // Action Mode
    private UpdateActionModeCallback updateActionModeCallback;
    private List<Object> selected;

    private static final DiffUtil.ItemCallback<Box> DIFF_CALLBACK = new DiffUtil.ItemCallback<Box>() {

        @Override
        public boolean areItemsTheSame(@NonNull Box oldItem, @NonNull Box newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Box oldItem, @NonNull Box newItem) {
            return oldItem.getId().equals(newItem.getId()) &&
                    oldItem.getNumber() == newItem.getNumber() &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getCategories().equals(newItem.getCategories()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getCreatedDate().equals(newItem.getCreatedDate());
        }
    };

    public BoxesAdapter(@NonNull Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.filter = createFilter();
    }

    public void setBoxesForFiltering(@NonNull List<Box> boxes) {
        this.boxesForFiltering = boxes;
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

    @Override
    public long getItemId(int position) {
        final Box box = getItem(position);
        return box.hashCode();
    }

    @NonNull
    @Override
    public BoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderBoxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_box, parent, false);
        return new BoxViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxViewHolder holder, int position) {
        final Box box = getItem(position);
        if (box.getDescription().equals("")) {
            holder.binding.description.setText(R.string.no_description);
        }
        holder.binding.setBox(box);
        holder.binding.setSelected(false);
    }

    @Override
    public Filter getFilter() {
        return this.filter;
    }

    private Filter createFilter() {
        return new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                List<Box> filtered;
                BoxPropertiesFilter pf = new BoxPropertiesFilter(boxesForFiltering);
                filtered = pf.filter(constraint, true, true, true);

                Filter.FilterResults results = new Filter.FilterResults();
                results.values = filtered;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                BoxesAdapter.this.submitList((List) results.values);
            }
        };
    }

    class BoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        ViewholderBoxBinding binding;

        BoxViewHolder(@NonNull ViewholderBoxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            this.binding.getRoot().setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (selected == null) {
                final Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", getItem(getAdapterPosition()).getId());
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.slide_in_from_right, R.anim.stay_still);
            } else {
                if (updateActionModeCallback != null) {
                    final Box box = getItem(getAdapterPosition());
                    if (selected.contains(box)) {
                        selected.remove(box);
                    } else {
                        selected.add(box);
                    }
                    binding.setSelected(!binding.getSelected());
                    updateActionModeCallback.update(selected, getObjectTypeString());
                }
            }
        }

        private String getObjectTypeString() {
            return (selected.size() < 1 || selected.size() > 1) ? "boxes" : "box";
        }

        @Override
        public boolean onLongClick(View v) {
            if (selected == null) {
                selected = new ArrayList<>();
                if (updateActionModeCallback != null) {
                    selected.add(getItem(getAdapterPosition()));
                    if (updateActionModeCallback.update(selected, getObjectTypeString())) {
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

