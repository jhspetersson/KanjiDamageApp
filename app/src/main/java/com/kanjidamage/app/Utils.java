package com.kanjidamage.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Utils {
    public static String join(JSONArray array) throws JSONException {
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
    }

    public static boolean isNotEmpty(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null || jsonArray.length() == 0) {
            return false;
        }

        for (int i = 0; i < jsonArray.length(); i++) {
            String value = jsonArray.getString(i);
            if (value != null && !value.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static boolean isNotEmpty(Collection<String> collection) {
        if (collection == null || collection.isEmpty()) {
            return false;
        }

        for (String value : collection) {
            if (value != null && !value.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    public static void openUrl(String url, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        context.startActivity(i);
    }
}
