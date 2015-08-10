package com.ghsoft.barometergraph.data;

/**
 * Created by brian on 8/4/15.
 */
public class CSVFileNameSanitizer {

    private static final String CSV = ".csv";

    public static String sanitize(String name) {
        name = name.replaceAll("/", "_").replaceAll("\0", " ");
        return name + CSV;
    }
}
