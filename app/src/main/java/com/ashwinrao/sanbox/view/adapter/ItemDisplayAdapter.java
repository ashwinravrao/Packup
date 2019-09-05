package com.ashwinrao.sanbox.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.sanbox.R;
import com.ashwinrao.sanbox.data.model.Item;
import com.ashwinrao.sanbox.databinding.ViewholderItemDisplayBinding;
import com.ashwinrao.sanbox.util.ItemPropertiesFilter;
import com.ashwinrao.sanbox.util.callback.UpdateActionModeCallback;
import com.ashwinrao.sanbox.view.activity.DetailActivity;
import com.ashwinrao.sanbox.view.activity.MainActivity;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ItemDisplayAdapter extends ListAdapter<Item, ItemDisplayAdapter.ItemViewHolder> implements Filterable {

    private Filter filter;
    private Context context;
    private List<Object> selected;
    private Boolean isShownWithBoxContext;
    private List<Item> itemsForFiltering = new ArrayList<>();

    // Callbacks
    private UpdateActionModeCallback updateActionModeCallback;

    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {

        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getBoxUUID().equals(newItem.getBoxUUID())
                    && oldItem.getEstimatedValue() == newItem.getEstimatedValue()
                    && oldItem.getName().equals(newItem.getName())
                    && oldItem.getPackedDate().equals(newItem.getPackedDate())
                    && oldItem.getFilePath().equals(newItem.getFilePath())
                    && oldItem.getBoxNumber() == newItem.getBoxNumber();
        }
    };

    public ItemDisplayAdapter(@NonNull Context context, @NonNull Boolean isShownWithBoxContext) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.isShownWithBoxContext = isShownWithBoxContext;
    }

    public void setItemsForFiltering(@NonNull List<Item> itemsForFiltering) {
        this.itemsForFiltering = itemsForFiltering;
    }

    public void initializeFilter() {
        this.filter = createFilter();
    }

    public void setActionModeCallback(@NonNull UpdateActionModeCallback callback) {
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
        final Item item = getItem(position);
        return item.hashCode();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemDisplayBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item_display, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.binding.setItem(getItem(position));
        holder.binding.setSelected(false);
        getItem(position).setIsShownWithBoxContext(isShownWithBoxContext);
        loadImage(context, getItem(position), holder.binding.itemImage);
    }

    private void loadImage(@NonNull Context context, @NonNull final Item item, @NonNull ImageView imageView) {
        final String filePath = item.getFilePath();
        Glide.with(context)
                .load(new File(filePath))
                .thumbnail(0.01f)  // down-sample to 1% of original resolution
                .centerCrop()
                .into(imageView);
    }

    @Override
    public Filter getFilter() {
        return this.filter;
    }

    private Filter createFilter() {
        return new Filter() {

            @Override
            protected Filter.FilterResults performFiltering(CharSequence constraint) {
                final List<Item> filtered;
                final ItemPropertiesFilter pf = new ItemPropertiesFilter(itemsForFiltering);
                filtered = pf.filter(constraint, true);

                final Filter.FilterResults results = new Filter.FilterResults();
                results.values = filtered;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                ItemDisplayAdapter.this.submitList((List) results.values);
            }
        };
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, Toolbar.OnMenuItemClickListener {

        ViewholderItemDisplayBinding binding;

        ItemViewHolder(@NonNull ViewholderItemDisplayBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
            this.binding.getRoot().setOnLongClickListener(this);
            this.binding.toolbar.inflateMenu(R.menu.toolbar_view_box);
            if (isShownWithBoxContext) this.binding.toolbar.setVisibility(View.GONE);
            this.binding.toolbar.setOnMenuItemClickListener(this);
            this.binding.toolbar.setOverflowIcon(ContextCompat.getDrawable(context, R.drawable.ic_overflow));
        }

        @Override
        public void onClick(View v) {
            if (selected != null && updateActionModeCallback != null) {
                if (selected.contains(getItem(getAdapterPosition()))) {
                    selected.remove(getItem(getAdapterPosition()));
                } else {
                    selected.add(getItem(getAdapterPosition()));
                }
                binding.setSelected(!binding.getSelected());
                updateActionModeCallback.update(selected, getObjectTypeString());
            }
        }

        private String getObjectTypeString() {
            return (selected.size() != 1) ? "items" : "item";
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

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.toolbar_view_box) {
                final Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("ID", getItem(getAdapterPosition()).getBoxUUID());
                context.startActivity(intent);
                ((MainActivity) context).overridePendingTransition(R.anim.slide_in_from_right, R.anim.stay_still);
                return true;
            }
            return false;
        }
    }

}
