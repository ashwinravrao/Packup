package com.ashwinrao.packup.util.callback;

import androidx.annotation.NonNull;

import com.ashwinrao.packup.data.model.Item;

public interface ItemEditedCallback {

    void itemEdited(@NonNull Item item, @NonNull Integer position);
}
