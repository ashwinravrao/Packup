package com.ashwinrao.boxray.util;

public class FilenameCreator {

    private String input;

    public FilenameCreator(String input) {
        this.input = input;
    }

    public String create() {
        if(input != null) {
            input = input.replaceAll("\\s+", "");   // spaces
            input = input.replaceAll("\\W+", "");   // special characters
            input = input + ".jpeg";
        }
        return input;
    }
}
