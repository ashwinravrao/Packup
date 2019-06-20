package com.ashwinrao.boxray.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import java.util.function.Function;

public class ConfirmationDialog {

    private static final String TAG = "Boxray";

    /**
     * @param context is the View context in which the dialog will be shown
     * @param content title, message, positive button title, negative button title
     * @param cancelable is a boolean value indicating whether the dialog should be cancellable by tapping outside view
     * @param buttonColors is an int array composed of (in order) the color resource id's for the positive and negative buttons
     * @param positiveButtonAction is a Java 8 Function that takes a DialogInterface as its single argument and returns nothing
     * @param negativeButtonAction is a Java 8 Function that takes a DialogInterface as its single argument and returns nothing
     */

    public static void make(Context context,
                            String[] content,
                            boolean cancelable,
                            int[] buttonColors,
                            Function<DialogInterface, Void> positiveButtonAction,
                            Function<DialogInterface, Void> negativeButtonAction) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(content[0])
                .setMessage(content[1])
                .setCancelable(cancelable)
                .setPositiveButton(content[2], (dialog1, which) -> positiveButtonAction.apply(dialog1))
                .setNegativeButton(content[3], (dialog12, which) -> negativeButtonAction.apply(dialog12))
                .create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(buttonColors[0]);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(buttonColors[1]);
    }
}
