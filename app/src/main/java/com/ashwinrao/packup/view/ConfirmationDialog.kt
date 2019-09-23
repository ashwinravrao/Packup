package com.ashwinrao.packup.view

import android.content.Context
import android.content.DialogInterface

import com.google.android.material.dialog.MaterialAlertDialogBuilder

import java.util.function.Function

object ConfirmationDialog {

    /**
     * @param context is the View context in which the dialog will be shown
     * @param content title, message, positive button title, negative button title
     * @param cancelable is a boolean value indicating whether the dialog should be cancellable by tapping outside view
     * @param buttonColors is an int array composed of (in order) the color resource id's for the positive and negative buttons
     * @param positiveButtonAction is a Java 8 Function that takes a DialogInterface as its single argument and returns nothing
     * @param negativeButtonAction is a Java 8 Function that takes a DialogInterface as its single argument and returns nothing
     */

    fun make(context: Context,
             content: Array<String>,
             cancelable: Boolean,
             buttonColors: IntArray,
             positiveButtonAction: Function<DialogInterface, Void>,
             negativeButtonAction: Function<DialogInterface, Void>) {
        val dialog = MaterialAlertDialogBuilder(context)
                .setTitle(content[0])
                .setMessage(content[1])
                .setCancelable(cancelable)
                .setPositiveButton(content[2]) { dialog1, _ -> positiveButtonAction.apply(dialog1) }
                .setNegativeButton(content[3]) { dialog12, _ -> negativeButtonAction.apply(dialog12) }
                .create()
        dialog.show()
        dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(buttonColors[0])
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(buttonColors[1])
    }
}
