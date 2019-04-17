package com.kanjidamage.app;

import java.util.Collection;

public class Utils {
    public static <T> String join(Collection<T> collection) {
        return join(", ", collection);
    }

    public static <T> String join(String delimeter, Collection<T> collection) {
        if (collection.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (T value : collection) {
            sb.append(value).append(delimeter);
        }

        return sb.substring(0, sb.length() - delimeter.length());
    }}
