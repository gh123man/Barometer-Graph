package com.ghsoft.barometergraph.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

/**
 * Created by brian on 8/2/15.
 */
public class DialogHelper {

    public static void showViewButtonDialog(Context context, View view, DialogInterface.OnClickListener callback) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(false).setPositiveButton("OK", callback).create().show();
    }

}
