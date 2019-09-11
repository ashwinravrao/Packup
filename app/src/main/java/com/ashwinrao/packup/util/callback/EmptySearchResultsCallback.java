package com.ashwinrao.packup.util.callback;

import androidx.annotation.NonNull;

public interface EmptySearchResultsCallback {

    void handleEmptyResults(@NonNull Integer numResults);

}
