package com.ashwinrao.locrate.util.callback;

import androidx.annotation.NonNull;

import java.util.List;

public interface DismissCallback {

    void onDismiss(@NonNull final List<String> selectedCategories);
}
