package com.kanjidamage.app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class Data {
    public static List<String> kanji = new ArrayList<>();
    public static List<Map<String, String>> data = Collections.emptyList();

    interface DataLoadCallback {
        void onDataLoaded();
    }
}
