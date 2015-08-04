package com.ghsoft.barometergraph.data;

/**
 * Created by brian on 8/4/15.
 */
public class CSVFileNameSanitizer {

    public static String sanitize(String name) {
        return name + ".csv";
    }
}
