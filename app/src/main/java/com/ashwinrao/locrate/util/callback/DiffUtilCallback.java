package com.ashwinrao.locrate.util.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.ashwinrao.locrate.data.model.Box;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

    private List<Box> oldBoxes;
    private List<Box> newBoxes;

    public DiffUtilCallback(@NonNull List<Box> oldBoxes, @NonNull List<Box> newBoxes) {
        this.oldBoxes = oldBoxes;
        this.newBoxes = newBoxes;
    }

    @Override
    public int getOldListSize() {
        return oldBoxes.size();
    }

    @Override
    public int getNewListSize() {
        return newBoxes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldBoxes.get(oldItemPosition).equals(newBoxes.get(newItemPosition));
    }
}
