package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.ViewholderItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private Context mContext;
    private List<String> mItems;

    public ItemAdapter(Context context) {
        mContext = context;
    }

    public void setItems(List<String> items) {
        mItems = items;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.viewholder_item, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        String item = mItems.get(position);
        holder.binding.item.setText(item);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        final ViewholderItemBinding binding;

        public ItemViewHolder(final ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
