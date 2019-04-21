package com.kanjidamage.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class ItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("json"));
            String labelString = jsonObject.getString("kanji");
            JSONObject okurigana = jsonObject.optJSONObject("okurigana");
            if (okurigana != null) {
                labelString = okurigana.optString("pre", "") + labelString + okurigana.optString("post", "");
            }

            String itemMeaning = jsonObject.getString("meaning");

            TextView label = findViewById(R.id.label);
            label.setText(labelString);

            TextView reading = findViewById(R.id.reading);
            String read = jsonObject.optString("reading");
            if (read != null && !read.isEmpty()) {
                if (okurigana != null) {
                    read = okurigana.optString("pre", "") + read + okurigana.optString("post", "");
                }
                reading.setText(read);
            } else {
                reading.setVisibility(View.GONE);
            }

            TextView meaning = findViewById(R.id.meaning);
            meaning.setText(itemMeaning);

            TableLayout components = findViewById(R.id.components);
            JSONArray comps = jsonObject.optJSONArray("components");
            if (comps != null && comps.length() > 0) {
                for (int i = 0; i < comps.length(); i++) {
                    TableRow row = new TableRow(this);
                    components.addView(row);

                    JSONObject comp = comps.getJSONObject(i);

                    final String compString = comp.optString("kanji", "");
                    TextView compKanji = new TextView(this);
                    compKanji.setText(compString);
                    compKanji.setTextAppearance(this, R.style.link);
                    compKanji.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra(MainActivity.KEYWORD_EXTRA, compString);
                            startActivity(intent);
                        }
                    });
                    row.addView(compKanji);

                    TextView compMeaning = new TextView(this);
                    compMeaning.setText(comp.optString("meaning", ""));
                    row.addView(compMeaning);
                }
            } else {
                components.setVisibility(View.GONE);
            }

            TextView description = findViewById(R.id.description);
            String descr = jsonObject.optString("description");
            if (descr != null && !descr.isEmpty()) {
                description.setText(descr);
            } else {
                description.setVisibility(View.GONE);
            }

            TextView onyomi = findViewById(R.id.onyomi);
            JSONArray on = jsonObject.optJSONArray("onyomi");
            if (on != null && on.length() > 0) {
                onyomi.setText(Utils.join(on));
            } else {
                TextView onyomiLabel = findViewById(R.id.onyomi_label);

                onyomiLabel.setVisibility(View.GONE);
                onyomi.setVisibility(View.GONE);
            }

            TextView mnemonic = findViewById(R.id.mnemonic);
            String mnemo = jsonObject.optString("mnemonic");
            if (mnemo != null && !mnemo.isEmpty()) {
                mnemonic.setText(mnemo);
            } else {
                TextView mnemonicLabel = findViewById(R.id.mnemonic_label);

                mnemonicLabel.setVisibility(View.GONE);
                mnemonic.setVisibility(View.GONE);
            }

            TableLayout kunyomi = findViewById(R.id.kunyomi);
            JSONArray kun = jsonObject.optJSONArray("kunyomi");
            if (kun != null && kun.length() > 0) {
                for (int i = 0; i < kun.length(); i++) {
                    TableRow row = new TableRow(this);
                    kunyomi.addView(row);

                    TableRow row2 = new TableRow(this);
                    kunyomi.addView(row2);

                    JSONObject kunReading = kun.getJSONObject(i);

                    TextView preParticle = new TextView(this);
                    preParticle.setText(kunReading.optString("preParticle", ""));
                    row.addView(preParticle);

                    TextView kreading = new TextView(this);
                    kreading.setText(kunReading.optString("reading", ""));
                    row.addView(kreading);

                    TextView kokurigana = new TextView(this);
                    kokurigana.setText(kunReading.optString("okurigana", ""));
                    row.addView(kokurigana);

                    TextView postParticle = new TextView(this);
                    postParticle.setText(kunReading.optString("postParticle", ""));
                    row.addView(postParticle);

                    TextView kunMeaning = new TextView(this);
                    kunMeaning.setText(kunReading.optString("meaning", ""));
                    row.addView(kunMeaning);

                    row2.addView(new TextView(this));

                    TextView kunDescription = new TextView(this);
                    kunDescription.setText(kunReading.optString("description", ""));
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                    layoutParams.span = 4;
                    row2.addView(kunDescription, layoutParams);
                }
            } else {
                TextView kunyomiLabel = findViewById(R.id.kunyomi_label);

                kunyomiLabel.setVisibility(View.GONE);
                kunyomi.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
