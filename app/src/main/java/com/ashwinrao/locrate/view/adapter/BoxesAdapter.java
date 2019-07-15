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
import com.ashwinrao.locrate.util.callback.DiffUtilCallback;
import com.ashwinrao.locrate.util.callback.UpdateActionModeCallback;
import com.ashwinrao.locrate.view.activity.DetailActivity;
import com.ashwinrao.locrate.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


public class BoxesAdapter extends RecyclerView.Adapter<BoxesAdapter.BoxViewHolder> implements Filterable {

    private Filter filter;
    private Context context;
    private List<Box> boxes;
    private List<Box> boxesCopy;

    // Action Mode
    private UpdateActionModeCallback updateActionModeCallback;
    private List<Object> selected;

    public BoxesAdapter(@NonNull Context context) {
        this.context = context;
        initializeFilter();
    }

    public void initializeFilter() {
        this.filter = createFilter();
    }

    public void setBoxes(@NonNull List<Box> boxes) {
        this.boxes = boxes;
        this.boxesCopy = new ArrayList<>(boxes);
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
    public BoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderBoxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_box, parent, false);
        return new BoxViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxViewHolder holder, int position) {
        final Box box = boxes.get(position);
        holder.binding.setBox(box);
        holder.binding.setSelected(false);
    }

    @Override
    public int getItemCount() {
        return boxes == null ? 0 : boxes.size();
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
                BoxPropertiesFilter pf = new BoxPropertiesFilter(boxesCopy);
                filtered = pf.filter(constraint, true, true, true);

                Filter.FilterResults results = new Filter.FilterResults();
                results.values = filtered;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                final DiffUtilCallback diffUtil = new DiffUtilCallback(new ArrayList<>(boxes), (List) results.values);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtil, true);

                boxes.clear();
                boxes.addAll((List) results.values);
                result.dispatchUpdatesTo(BoxesAdapter.this);
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
            if(selected == null) {
                final Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", boxes.get(getAdapterPosition()).getId());
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.slide_in_from_right, R.anim.stay_still);
            } else {
                if(updateActionModeCallback != null) {
                    final Box box = boxes.get(getAdapterPosition());
                    if(selected.contains(box)) {
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
            if(selected == null) {
                selected = new ArrayList<>();
                if (updateActionModeCallback != null) {
                    selected.add(boxes.get(getAdapterPosition()));
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

