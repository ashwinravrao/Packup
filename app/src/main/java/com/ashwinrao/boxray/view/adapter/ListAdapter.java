package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.ViewholderBoxNewBinding;
import com.ashwinrao.boxray.util.ListChangeListener;
import com.ashwinrao.boxray.util.PropertiesFilter;
import com.ashwinrao.boxray.util.Utilities;
import com.ashwinrao.boxray.view.DetailFragment;
import com.ashwinrao.boxray.view.MainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
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

        RecyclerView recyclerView = holder.binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        Utilities.simpleItemDecoration(context, recyclerView, false);
        ThumbnailAdapter adapter = new ThumbnailAdapter(context, Utilities.dpToPx(context, 64f), Utilities.dpToPx(context, 64f));
        adapter.setPaths(box.getContents().subList(0, 3));
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                holder.onClick(rv);
                return true;
            }
        });

        // update x-items more TextView
        if(box.getContents().size() > 3) {
            holder.binding.xMoreItemsText.setText(String.format(Locale.US, "+ %d\nmore", box.getContents().size() - 3));
        } else {
            holder.binding.xMoreItemsText.setVisibility(View.GONE);
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
