package com.kanjidamage.app;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class MainActivity extends AppCompatActivity {

    private RecyclerView cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText search = findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcherHelper() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = String.valueOf(s);
                updateSearchResults(keyword);
            }
        });

        cards = findViewById(R.id.cards);
        cards.setLayoutManager(new LinearLayoutManager(this));

        loadData();
    }

    private void updateSearchResults(String keyword) {
        cards.setAdapter(new DataAdapter(filter(keyword)));
    }

    private List<Map<String, String>> filter(String keyword) {
        List<Map<String, String>> result = new ArrayList<>();

        if (!keyword.isEmpty()) {
            for (Map<String, String> row : Data.data) {
                if (row.get("label").contains(keyword)) {
                    result.add(row);
                }
            }
        }

        return result;
    }

    private void loadData() {
        if (Data.data.isEmpty()) {
            new LoadDataTask().doInBackground(this);
        }
    }

    private static class LoadDataTask extends AsyncTask<Context, Void, Void> {
        @Override
        protected Void doInBackground(Context... params) {
            JSONObject json = loadJSONFromAsset(params[0]);
            List<Map<String, String>> data = new ArrayList<>();

            try {
                JSONArray kanjis = json.getJSONArray("kanji");
                for (int i = 0; i < kanjis.length(); i++) {
                    JSONObject kanji = kanjis.getJSONObject(i);
                    Map<String, String> row = new HashMap<>();
                    row.put("label", kanji.getString("kanji"));
                    row.put("description", kanji.getString("meaning"));

                    List<String> kun = new ArrayList<>();
                    JSONArray kunyomis = kanji.optJSONArray("kunyomi");
                    if (kunyomis != null) {
                        for (int j = 0; j < kunyomis.length(); j++) {
                            JSONObject kunyomi = kunyomis.getJSONObject(j);
                            String reading = kunyomi.getString("reading");
                            String okurigana = kunyomi.optString("okurigana");
                            if (okurigana == null || okurigana.isEmpty()) {
                                kun.add(reading);
                            } else {
                                kun.add(reading + "." + okurigana);
                            }
                        }
                    }

                    List<String> on = new ArrayList<>();
                    JSONArray onyomis = kanji.optJSONArray("onyomi");
                    if (onyomis != null) {
                        for (int j = 0; j < onyomis.length(); j++) {
                            on.add(onyomis.getString(j));
                        }
                    }

                    String comment = "";
                    if (!kun.isEmpty()) {
                        comment += Utils.join(kun);

                        if (!on.isEmpty()) {
                            comment += " / ";
                        }
                    }

                    if (!on.isEmpty()) {
                        comment += Utils.join(on);
                    }

                    row.put("comment", comment);

                    data.add(row);
                }

                JSONArray jukugos = json.getJSONArray("jukugo");
                for (int i = 0; i < jukugos.length(); i++) {
                    JSONObject jukugo = jukugos.getJSONObject(i);
                    Map<String, String> row = new HashMap<>();
                    row.put("label", jukugo.getString("kanji"));
                    row.put("description", jukugo.getString("meaning"));
                    row.put("comment", jukugo.getString("reading"));
                    data.add(row);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Data.data = data;

            return null;
        }
    }

    public static JSONObject loadJSONFromAsset(Context context) {
        try {
            InputStream is = context.getAssets().open("kanjidamage.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            return new JSONObject(json);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
