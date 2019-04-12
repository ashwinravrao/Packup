package com.ashwinrao.boxray.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Utilities {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

    // Convenience method to calculate pixel offsets from the density independent variety
    public static float dpToPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

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

    // Detect soft keyboard
    public static boolean keyboardIsShowing(@NonNull View root) {
        Rect r = new Rect();
        root.getWindowVisibleDisplayFrame(r);
        int screenHeight = root.getRootView().getHeight();
        int keypadHeight = screenHeight - r.bottom;
        return keypadHeight > screenHeight * 0.15;
    }

}
