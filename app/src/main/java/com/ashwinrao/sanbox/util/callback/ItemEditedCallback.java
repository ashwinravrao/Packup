package com.ashwinrao.sanbox.util.callback;

import androidx.annotation.NonNull;

import com.ashwinrao.sanbox.data.model.Item;

public interface ItemEditedCallback {

    void itemEdited(@NonNull Item item, @NonNull Integer position);
}
