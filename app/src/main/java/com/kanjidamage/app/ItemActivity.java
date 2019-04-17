package com.kanjidamage.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONObject;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
            String kanji = jsonObject.getString("kanji");
            String meaning = jsonObject.getString("meaning");


            TextView label = findViewById(R.id.label);
            label.setText(kanji);

            TextView description = findViewById(R.id.description);
            description.setText(meaning);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
