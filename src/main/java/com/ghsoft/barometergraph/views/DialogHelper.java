package com.ghsoft.barometergraph.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.WindowManager;

import com.ghsoft.barometergraph.R;

/**
 * Created by brian on 8/4/15.
 */
public class DialogHelper {

    public static void showEditDialog(Context context, View v, DialogInterface.OnClickListener callback, boolean cancelable) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(v);
        alertDialogBuilder.setCancelable(cancelable).setPositiveButton(context.getString(R.string.ok), callback);
        AlertDialog dialog;
        dialog = alertDialogBuilder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }
}
