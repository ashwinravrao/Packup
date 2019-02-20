package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.ViewholderBoxBinding;
import com.ashwinrao.boxray.view.DetailFragment;
import com.ashwinrao.boxray.view.MainActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.BoxViewHolder> {

    private Context mContext;
    private List<Box> mBoxes;

    public ListAdapter(@NonNull Context context, @Nullable List<Box> boxes) {
        mContext = context;
        mBoxes = boxes;
    }

    public void setBoxes(List<Box> boxes) {
        mBoxes = boxes;
    }

    @NonNull
    @Override
    public BoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderBoxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.viewholder_box, parent, false);
        return new BoxViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxViewHolder holder, int position) {
        Box box = mBoxes.get(position);
        holder.binding.setBox(box);
        holder.binding.numItemsTextView.setText(mContext.getString(R.string.box_num_items_placeholder, box.getContents().size()));
    }

    @Override
    public int getItemCount() {
        return mBoxes == null ? 0 : mBoxes.size();
    }

    public class BoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewholderBoxBinding binding;

        public BoxViewHolder(@NonNull ViewholderBoxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // todo pass box id via Bundle, retrieve corresponding box in DetailFragment via BoxViewModel
            ((MainActivity) mContext)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.fragment_container, new DetailFragment(), "DetailFragment")
                    .commit();
        }
    }
}
