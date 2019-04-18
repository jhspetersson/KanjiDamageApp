package com.kanjidamage.app;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    public static <T> String join(JSONArray array) throws JSONException {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getString(i));
        }

        return join(list);
    }

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
