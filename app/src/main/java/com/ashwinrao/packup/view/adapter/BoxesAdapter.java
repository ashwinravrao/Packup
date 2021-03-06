package com.ashwinrao.packup.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ashwinrao.packup.R;
import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.databinding.ViewholderBoxBinding;
import com.ashwinrao.packup.util.BoxPropertiesFilter;
import com.ashwinrao.packup.util.callback.EmptySearchResultsCallback;
import com.ashwinrao.packup.util.callback.UpdateActionModeCallback;
import com.ashwinrao.packup.view.activity.DetailActivity;
import com.ashwinrao.packup.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;


public class BoxesAdapter extends ListAdapter<Box, BoxesAdapter.BoxViewHolder> implements Filterable {

    private Filter filter;
    private Context context;
    private List<Box> boxesForFiltering = new ArrayList<>();
    private EmptySearchResultsCallback emptySearchResultsCallback;

    // Shared Element Activity Transitions
    private Pair[] viewsToTransition;

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

    public void setBoxesForFiltering(@Nullable List<Box> boxes) {
        this.boxesForFiltering = boxes;
    }

    public void setUpdateActionModeCallback(@NonNull UpdateActionModeCallback callback) {
        this.updateActionModeCallback = callback;
    }

    public void setEmptySearchResultsCallback(@NonNull EmptySearchResultsCallback callback) {
        this.emptySearchResultsCallback = callback;
    }

    public void setViewsToTransition(@NonNull Pair[] viewsToTransition) {
        this.viewsToTransition = viewsToTransition;
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
            holder.binding.boxDescription.setText(R.string.no_description);
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
                emptySearchResultsCallback.handleEmptyResults(boxesForFiltering.size() > 0 ? ((List) results.values).size() : 1);
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
                final ActivityOptionsCompat optionsCompat = makeActivityOptions();
                context.startActivity(intent, optionsCompat.toBundle());
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

        @SuppressWarnings("unchecked")
        private ActivityOptionsCompat makeActivityOptions() {
            final Pair<View, String> boxNamePair = Pair.create(binding.boxName, context.getString(R.string.box_name_transition));
            final Pair<View, String> boxDescriptionPair = Pair.create(binding.boxDescription, context.getString(R.string.box_description_transition));
            return ActivityOptionsCompat.makeSceneTransitionAnimation(
                    (MainActivity) context,
                    boxNamePair,
                    boxDescriptionPair,
                    viewsToTransition[0],
                    viewsToTransition[1]
            );
        }

        private String getObjectTypeString() {
            return (selected.size() != 1) ? "boxes" : "box";
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

