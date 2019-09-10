package com.ashwinrao.packup.util.callback;

import androidx.annotation.NonNull;

import java.util.List;

public interface DialogDismissedCallback {

    void onDialogDismissed(@NonNull final List<String> selectedCategories);
}