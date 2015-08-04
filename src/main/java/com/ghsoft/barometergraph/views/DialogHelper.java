package com.ghsoft.barometergraph.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by brian on 8/4/15.
 */
public class DialogHelper {

    public static void showEditDialog(Context context, View v, DialogInterface.OnClickListener callback) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", callback);
        AlertDialog dialog;
        dialog = alertDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
}
