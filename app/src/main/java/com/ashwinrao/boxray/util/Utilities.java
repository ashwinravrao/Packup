package com.ashwinrao.boxray.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Utilities {

    /**
     * @param activity (required)
     * @param originalSoftInputMode (optional)
     *
     * Temporarily toggles application's soft input mode for the lifespan of the calling fragment.
     * This is done by storing the current soft input mode, temporarily setting it to adjust nothing,
     * and restoring the original value onDestroy().
     *
     * Note: the reason we don't globally fix the manifest soft input mode is because different
     * fragments have different intended behaviors that leverage soft input modes. For example,
     * the behavior of the background image when a SearchView is inflated in an action bar versus
     * the behavior of a button when an EditText field gains focus in a particularly long NestedScrollView.
     *
     * @return original Android Manifest soft input mode
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

    // Manually hide software keyboard
    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    // Manually hide software keyboard (alternate method)
//    public static void hideKeyboardFromAlt(Activity activity) {
//        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
//    }

}
