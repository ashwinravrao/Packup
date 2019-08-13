package com.ashwinrao.locrate.util.callback;

import androidx.annotation.NonNull;

import com.ashwinrao.locrate.data.model.Item;

public interface SingleItemUnpackCallback {

    void unpackItem(@NonNull final Item item, @NonNull final Integer position);
}
