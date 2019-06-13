package com.ashwinrao.boxray.util;

public class FilenameCreator {

    public static String create(String input) {
        if(input != null) {
            input = input.replaceAll("\\s+", "");   // spaces
            input = input.replaceAll("\\W+", "");   // special characters
            input = "/" + input + ".jpeg";
        }
        return input;
    }
}
