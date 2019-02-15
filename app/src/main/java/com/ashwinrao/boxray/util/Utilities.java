package com.ashwinrao.boxray.util;

import android.view.WindowManager;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Utilities {

    /**
     * @param activity
     * @param originalSoftInputMode
     *
     * Saves current window softInputMode (from AndroidManifest),
     * and changes to WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING.
     *
     * In onDestroy() this method is called again to restore the original config to the Manifest.
     *
     * @return manifest WindowManager.Attributes.SoftInputMode from before this Fragment was inflated.
     */

    public static int applyFragmentSoftInput(FragmentActivity activity, @Nullable Integer originalSoftInputMode) {
        FragmentActivity act = Objects.requireNonNull(activity);
        if(originalSoftInputMode != null) {
            activity.getWindow().setSoftInputMode(originalSoftInputMode);
            return -1;
        } else {
            int original = act.getWindow().getAttributes().softInputMode;
            act.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
            return original;
        }
    }

}
