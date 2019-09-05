package com.ashwinrao.sanbox.util;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashtagDetection {

    public static void detect(@NonNull CharSequence s, @NonNull List<String> categories, @NonNull Boolean[] matchFound, @NonNull String[] matchStrings) {

        // Reset match found flags
        matchFound[0] = false;
        matchStrings[0] = null;
        matchStrings[1] = null;

        final Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+ )").matcher(s);
        while (matcher.find()) {
            matchFound[0] = true;
            matchStrings[0] = s.subSequence(matcher.start(), matcher.end()).toString();
            matchStrings[1] = s.subSequence(matcher.start()+1, matcher.end()-1).toString();
            categories.add(matchStrings[1]);
        }
    }
}
