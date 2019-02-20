package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.databinding.ViewholderItemBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private View mActivityRoot;
    private Context mContext;
    private List<String> mAdapterItems;
    private MutableLiveData<List<String>> mFragmentItems;

    public ItemAdapter(Context context, View activityRoot, MutableLiveData<List<String>> fragmentItems) {
        mContext = context;
        mActivityRoot = activityRoot;
        mFragmentItems = fragmentItems;
    }

    public void setAdapterItems(List<String> adapterItems) {
        mAdapterItems = adapterItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.viewholder_item, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        String item = mAdapterItems.get(position);
        holder.binding.item.setText(item);
        handleItemRemoval(holder.binding.itemRemoveButton, position);
    }

    private void handleItemRemoval(@NonNull View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String item = mAdapterItems.get(position);
                mAdapterItems.remove(position);
                notifyItemRemoved(position);
                mFragmentItems.setValue(mAdapterItems);
                Snackbar.make(mActivityRoot, mContext.getString(R.string.snackbar_item_deleted), Snackbar.LENGTH_LONG)
                        .setAction(mContext.getString(R.string.Undo), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mAdapterItems.add(position, item);
                                notifyItemInserted(position);
                                mFragmentItems.setValue(mAdapterItems);
                            }
                        })
                        .setActionTextColor(mContext.getColor(R.color.colorAccent))
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdapterItems == null ? 0 : mAdapterItems.size();
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        final ViewholderItemBinding binding;

        public ItemViewHolder(final ViewholderItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
