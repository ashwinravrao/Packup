package com.ashwinrao.sanbox.util.callback;

import androidx.annotation.NonNull;

import com.ashwinrao.sanbox.data.model.Item;

public interface SingleItemUnpackCallback {

    void unpackItem(@NonNull final Item item, @NonNull final Integer position);
}
