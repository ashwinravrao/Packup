package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.ViewholderBoxNewBinding;
import com.ashwinrao.boxray.util.ListChangeListener;
import com.ashwinrao.boxray.util.PropertiesFilter;
import com.ashwinrao.boxray.view.DetailFragment;
import com.ashwinrao.boxray.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.BoxViewHolder> implements Filterable {

    private Context context;
    private List<Box> boxes;
    private List<Box> boxesCopy;
    private ListChangeListener listener;

    private static final String TAG = "ListAdapter";

    public ListAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void registerListChangeListener(ListChangeListener listener) {
        this.listener = listener;
    }

    public void unregisterListChangeListener() {
        this.listener = null;
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
        addContentPreviews(box, holder.binding);
    }

    private void addContentPreviews(Box box, ViewholderBoxNewBinding binding) {
        binding.contentPreviewContainer.removeAllViews();
        final List<String> contents = new ArrayList<>();
        if(box.getContents() != null) {
            contents.addAll(box.getContents());
        }

        int numPreviews = contents.size() > 3 ? 3 : contents.size();
        if(contents.size() <= 3) {
            binding.xMoreItems.setVisibility(View.GONE);
        } else {
            binding.xMoreItemsText.setText(String.format(Locale.US, "+ %d more", contents.size() - 3));
        }

        for(int i = 0; i < numPreviews; i++) {
            Log.e(TAG, "onBindViewHolder: " + numPreviews);
            LayoutInflater.from(context).inflate(R.layout.content_preview, binding.contentPreviewContainer);
        }
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
            List<Box> filtered;
            PropertiesFilter pf = new PropertiesFilter(boxesCopy);
            filtered = pf.filter(constraint, true, true, true);

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
