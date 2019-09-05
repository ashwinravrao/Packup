package com.ashwinrao.sanbox.util.callback;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {

    private List<Object> oldList;
    private List<Object> newList;

    public DiffUtilCallback(@NonNull List<Object> oldList, @NonNull List<Object> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldItemPosition == newItemPosition;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    public int getIndexOfDeletion() {
        for (int i = 0; i < getOldListSize(); i++) {
            if (!oldList.get(i).equals(newList.get(i))) {
                return i;
            }
        }
        return getNewListSize() + 1;
    }
}
