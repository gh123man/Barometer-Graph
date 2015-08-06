package com.ghsoft.barometergraph.data;

/**
 * Created by brian on 8/4/15.
 */
public class CSVFileNameSanitizer {

    public static String sanitize(String name) {
        name = name.replaceAll("/", "_").replaceAll("\0", " ");
        return name + ".csv";
    }
}
