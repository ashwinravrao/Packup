package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.databinding.ViewholderItemPackingBinding;
import com.ashwinrao.locrate.util.callback.ItemEditedCallback;
import com.ashwinrao.locrate.util.callback.SingleItemUnpackCallback;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;


public class ItemPackAdapter extends RecyclerView.Adapter<ItemPackAdapter.ItemViewHolder> {

    private Context context;
    private List<Item> items;
    private final boolean[] firstBind = {true};
    private MutableLiveData<Item> editedItem = new MutableLiveData<>();
    private SingleItemUnpackCallback singleItemUnpackCallback;
    private ItemEditedCallback itemEditedCallback;

    private final String TAG = this.getClass().getSimpleName();

    public ItemPackAdapter(@NonNull Context context) {
        this.context = context;
    }

    /**
     * For use by external classes.
     *
     * @return edited item to be persisted by a LifecycleOwner.
     */

    public LiveData<Item> getEditedItem() {
        return editedItem;
    }

    public void setFirstBind(boolean bindComplete) {
        this.firstBind[0] = bindComplete;
    }

    public void setItems(@NonNull List<Item> items) {
        this.items = items;
    }

    public void setSingleItemUnpackCallback(@NonNull SingleItemUnpackCallback callback) {
        this.singleItemUnpackCallback = callback;
    }

    public void setItemEditedCallback(@NonNull ItemEditedCallback callback) {
        this.itemEditedCallback = callback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewholderItemPackingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_item_packing, parent, false);
        return new ItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final Item item = items.get(position);
        if (item.getName().length() > 0) {
            holder.binding.itemNameEditText.setText(item.getName());
        }
        if (item.getEstimatedValue() > 0.0) {
            holder.binding.estimatedValueEditText.setText(String.valueOf(item.getEstimatedValue()));
        }
        Glide.with(context)
                .load(new File(item.getFilePath()))
                .thumbnail(0.01f)  // downsample to 1% of original resolution
                .centerCrop()
                .into(holder.binding.itemImage);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewholderItemPackingBinding binding;

        ItemViewHolder(@NonNull ViewholderItemPackingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.unpackButton.setOnClickListener(this);
            this.binding.itemNameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!firstBind[0]) {
                        items.get(getAdapterPosition()).setName(s.toString().length() > 0 ? s.toString() : null);
                        if(itemEditedCallback != null) itemEditedCallback.itemEdited(items.get(getAdapterPosition()), getAdapterPosition());
                        editedItem.setValue(items.get(getAdapterPosition()));
                    }
                }
            });
            this.binding.estimatedValueEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(!firstBind[0]) {
                        items.get(getAdapterPosition()).setEstimatedValue(Double.valueOf(s.toString().length() > 0 ? s.toString() : "0.0"));
                        editedItem.setValue(items.get(getAdapterPosition()));
                        if(itemEditedCallback != null) itemEditedCallback.itemEdited(items.get(getAdapterPosition()), getAdapterPosition());
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {
            if(singleItemUnpackCallback != null) singleItemUnpackCallback.unpackItem(items.get(getAdapterPosition()), getAdapterPosition());
        }
    }

}
