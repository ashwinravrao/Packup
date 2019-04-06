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
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context context;
    private List<String> items;
    private View viewForSnackbar;
    private MutableLiveData<List<String>> fragmentItems;

    public ItemAdapter(@NonNull Context context, @NonNull View viewForSnackbar, @NonNull MutableLiveData<List<String>> fragmentItems, @Nullable List<String> items) {
        this.context = context;
        this.viewForSnackbar = viewForSnackbar;
        this.items = new ArrayList<>();
        this.fragmentItems = fragmentItems;
        if(items != null) { this.items = items; }
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.binding.savedItemLabel.setText(item);
        handleItemRemoval(holder.binding.savedItemRemoveButton, position);
    }

    private void handleItemRemoval(@NonNull View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String item = items.get(position);
                items.remove(position);
                notifyItemRemoved(position);
                fragmentItems.setValue(items);
                Snackbar.make(viewForSnackbar, context.getString(R.string.snackbar_item_deleted), Snackbar.LENGTH_LONG)
                        .setAction(context.getString(R.string.message_undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                items.add(position, item);
                                notifyItemInserted(position);
                                fragmentItems.setValue(items);
                            }
                        })
                        .setActionTextColor(context.getColor(R.color.colorAccent))
                        .show();
            }
        });
    }



    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final ViewholderItemBinding binding;

        public ViewHolder(final ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
