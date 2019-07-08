package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.os.Bundle;
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
import com.ashwinrao.locrate.view.fragment.DetailFragment;
import com.ashwinrao.locrate.view.activity.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


public class BoxesAdapter extends RecyclerView.Adapter<BoxesAdapter.BoxViewHolder> implements Filterable {

    private Context context;
    private List<Box> boxes;
    private List<Box> boxesCopy;

    public BoxesAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setBoxes(@NonNull List<Box> boxes) {
        this.boxes = boxes;
        this.boxesCopy = new ArrayList<>(boxes);
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
    }

    @Override
    public int getItemCount() {
        return boxes == null ? 0 : boxes.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Box> filtered;
                BoxPropertiesFilter pf = new BoxPropertiesFilter(boxesCopy);
                filtered = pf.filter(constraint, true, true, true);

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                final DiffUtilCallback diffUtil = new DiffUtilCallback(new ArrayList<>(boxes), (List) results.values);
                DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtil, true);

                boxes.clear();
                boxes.addAll((List) results.values);
                result.dispatchUpdatesTo(BoxesAdapter.this);
            }
        };
    }

    class BoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewholderBoxBinding binding;

        BoxViewHolder(@NonNull ViewholderBoxBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putInt("ID", boxes.get(getAdapterPosition()).getId());
            DetailFragment detail = new DetailFragment();
            detail.setArguments(bundle);

            ((MainActivity) context)
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .setCustomAnimations(R.anim.slide_in_from_right, R.anim.stay_still, R.anim.stay_still, R.anim.slide_out_to_right)
                    .replace(R.id.fragment_container, detail)
                    .commit();
        }
    }
}

