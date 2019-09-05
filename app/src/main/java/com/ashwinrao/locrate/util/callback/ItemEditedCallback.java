package com.ashwinrao.locrate.util.callback;

import androidx.annotation.NonNull;

import com.ashwinrao.locrate.data.model.Item;

public interface ItemEditedCallback {

    void itemEdited(@NonNull Item item, @NonNull Integer position);
}
