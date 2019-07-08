package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ViewholderItemBinding;
import com.ashwinrao.locrate.util.ItemPropertiesFilter;
import com.ashwinrao.locrate.util.callback.DiffUtilCallback;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> implements Filterable {

    private Context context;
    private List<Item> items;
    private List<Item> itemsCopy;

    public ItemsAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setItems(@NonNull List<Item> items) {
        this.items = items;
        this.itemsCopy = new ArrayList<>(items);

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
//        Item item = items.get(position);
//        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Item> filtered;
                ItemPropertiesFilter pf = new ItemPropertiesFilter(itemsCopy);
                filtered = pf.filter(constraint, true);

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                final DiffUtilCallback diffUtil = new DiffUtilCallback(new ArrayList<>(items), (List) results.values);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtil, true);

                items.clear();
                items.addAll((List) results.values);
                result.dispatchUpdatesTo(ItemsAdapter.this);
            }
        };
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewholderItemBinding binding;

        ItemViewHolder(@NonNull ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @Override
        public void onClick(View v) {
            // TODO replace with actual action
            Toast.makeText(context, "You clicked this item", Toast.LENGTH_SHORT).show();
        }
    }

}
