package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.ViewholderBoxBinding;
import com.ashwinrao.boxray.databinding.ViewholderBoxNewBinding;
import com.ashwinrao.boxray.util.ContextualAppBarListener;
import com.ashwinrao.boxray.view.DetailFragment;
import com.ashwinrao.boxray.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import javax.inject.Singleton;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.BoxViewHolder> implements Filterable {

    private Context context;
    private List<Box> boxes;
    private List<Box> boxesCopy;

    public ListAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setBoxes(List<Box> boxes) {
        this.boxes = boxes;
        this.boxesCopy = new ArrayList<>(boxes);
    }

    @NonNull
    @Override
    public BoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderBoxNewBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_box_new, parent, false);
        return new BoxViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BoxViewHolder holder, int position) {
        Box box = boxes.get(position);
        holder.binding.setBox(box);
    }

    @Override
    public int getItemCount() {
        return boxes == null ? 0 : boxes.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Box> filtered = new ArrayList<>();
            if(constraint == null || constraint.length() < 1) {
                filtered.addAll(boxesCopy);
            } else {
                String filterText = constraint.toString().toLowerCase().trim();
                for(Box box : boxesCopy) {
                    if(box.getName().contains(filterText)) {
                        filtered.add(box);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered;
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            boxes.clear();
            boxes.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class BoxViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ViewholderBoxNewBinding binding;

        public BoxViewHolder(@NonNull ViewholderBoxNewBinding binding) {
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
