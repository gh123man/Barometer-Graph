package com.ghsoft.barometergraph.intents;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

/**
 * Created by brian on 8/4/15.
 */
public class CSVIntentHelper {

    public static Intent get(File file) {
        Intent i = new Intent();
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setAction(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        i.setType("text/csv");
        return i;
    }
}
