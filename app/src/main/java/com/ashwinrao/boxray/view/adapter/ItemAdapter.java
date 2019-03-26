package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.ViewholderItemBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private View activityRoot;
    private Context context;
    private List<String> adapterItems = new ArrayList<>();
    private MutableLiveData<List<String>> fragmentItems;

    public ItemAdapter(Context context, View activityRoot, MutableLiveData<List<String>> fragmentItems) {
        this.context = context;
        this.activityRoot = activityRoot;
        this.fragmentItems = fragmentItems;
    }

    public void setAdapterItems(List<String> adapterItems) {
        this.adapterItems = adapterItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        String item = adapterItems.get(position);
        holder.binding.item.setText(item);
        handleItemRemoval(holder.binding.itemRemoveButton, position);
    }

    private void handleItemRemoval(@NonNull View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String item = adapterItems.get(position);
                adapterItems.remove(position);
                notifyItemRemoved(position);
                fragmentItems.setValue(adapterItems);
                Snackbar.make(activityRoot, context.getString(R.string.snackbar_item_deleted), Snackbar.LENGTH_LONG)
                        .setAction(context.getString(R.string.Undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adapterItems.add(position, item);
                                notifyItemInserted(position);
                                fragmentItems.setValue(adapterItems);
                            }
                        })
                        .setActionTextColor(context.getColor(R.color.colorAccent))
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return adapterItems == null ? 0 : adapterItems.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        final ViewholderItemBinding binding;

        public ItemViewHolder(final ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
