package com.ashwinrao.packup.util.callback;

import androidx.annotation.NonNull;

import com.ashwinrao.packup.data.model.Item;

public interface SingleItemUnpackCallback {

    void unpackItem(@NonNull final Item item, @NonNull final Integer position);
}
