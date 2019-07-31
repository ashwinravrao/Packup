package com.ashwinrao.locrate.util.callback;

import androidx.annotation.NonNull;

import java.util.List;

public interface DialogDismissedCallback {

    void onDialogDismissed(@NonNull final List<String> selectedCategories);
}
