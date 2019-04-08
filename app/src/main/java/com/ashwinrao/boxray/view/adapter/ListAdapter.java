package com.ashwinrao.boxray.view.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashwinrao.boxray.R;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.databinding.ViewholderBoxBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.BoxViewHolder> {

        private Context context;
        private List<Box> boxes;
        private View viewForSnackbar;

        public ListAdapter(@NonNull Context context, @NonNull View viewForSnackbar, @Nullable List<Box> boxes) {
            this.context = context;
            this.boxes = boxes;
            this.viewForSnackbar = viewForSnackbar;
        }

        public void setBoxes(List<Box> boxes) {
            this.boxes = boxes;
        }

        @NonNull
        @Override
        public BoxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ViewholderBoxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_box, parent, false);
            return new BoxViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull BoxViewHolder holder, int position) {
            Box box = boxes.get(position);
            holder.binding.setBox(box);
            holder.binding.boxNumberTextView.setText(String.valueOf(box.getId()));
            holder.binding.numItemsTextView.setText(box.getContents().size() == 0 ? "No items" : context.getString(R.string.box_num_items_placeholder, box.getContents().size()));
        }

        @Override
        public int getItemCount() {
            return boxes == null ? 0 : boxes.size();
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
                Snackbar.make(viewForSnackbar, "Preparing to open " + binding.getBox().getName() + "...", Snackbar.LENGTH_LONG).show(); // todo replace with detail view implementation
//                Bundle bundle = new Bundle();
//                bundle.putInt("Box Number", boxes.get(getAdapterPosition()).getId());
//                DetailFragment detail = new DetailFragment();
//                detail.setArguments(bundle);
//
//                ((MainActivity) context)
//                        .getSupportFragmentManager()
//                        .beginTransaction()
//                        .addToBackStack(null)
//                        .setCustomAnimations(R.anim.slide_in_from_right, R.anim.stay_still, R.anim.stay_still, R.anim.slide_out_to_right)
//                        .replace(R.id.fragment_container_main, detail, "DetailFragment")
//                        .commit();
            }
        }
}
