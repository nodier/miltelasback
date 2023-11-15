package com.softlond.base.util;

import org.springframework.stereotype.Service;

@Service
public class StringUtils {

    public String padStart(String str, String fillWith, Integer length) {
        if (str.length() >= length) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        while (builder.length() < length - str.length()) {
            builder.append(fillWith);
        }
        builder.append(str);
        return builder.toString();
    }
}
