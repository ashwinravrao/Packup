package com.ashwinrao.locrate.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ashwinrao.locrate.R;
import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.databinding.ViewholderMoveBinding;

import java.util.List;

public class MovesAdapter extends RecyclerView.Adapter<MovesAdapter.VH> {

    private List<Move> moves;
    private final Context context;

    public MovesAdapter(@NonNull Context context) {
        this.context = context;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderMoveBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.viewholder_move, parent, false);
        return new VH(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        holder.binding.setMove(moves.get(position));
    }

    @Override
    public int getItemCount() {
        return moves == null ? 0 : moves.size();
    }

    class VH extends RecyclerView.ViewHolder implements View.OnClickListener {

        ViewholderMoveBinding binding;

        VH(@NonNull ViewholderMoveBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

}
