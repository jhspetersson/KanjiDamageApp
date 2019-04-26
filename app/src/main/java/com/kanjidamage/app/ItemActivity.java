package com.kanjidamage.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Space;
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
            String labelString = jsonObject.getString("k");
            JSONObject okurigana = jsonObject.optJSONObject("o");
            if (okurigana != null) {
                labelString = okurigana.optString("pre", "") + labelString + okurigana.optString("post", "");
            }

            TextView label = findViewById(R.id.label);
            label.setText(labelString);
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });

            TextView reading = findViewById(R.id.reading);
            String read = jsonObject.optString("r");
            if (read != null && !read.isEmpty()) {
                if (okurigana != null) {
                    read = okurigana.optString("pre", "") + read + okurigana.optString("post", "");
                }
                reading.setText(read);
            } else {
                reading.setVisibility(View.GONE);
            }

            LinearLayout tagsHolder = findViewById(R.id.tags_holder);
            JSONArray tags = jsonObject.optJSONArray("t");
            if (tags != null) {
                for (int i = 0; i < tags.length(); i++) {
                    String tag = tags.getString(i);

                    TextView tagView = new TextView(this);
                    tagView.setText(tag);
                    tagView.setTextColor(Color.WHITE);
                    tagView.setTextAppearance(this, R.style.tag);
                    tagView.setBackgroundResource(R.drawable.rounded_corners);
                    ((GradientDrawable) tagView.getBackground()).setColor(Color.parseColor("#2d6987"));

                    tagsHolder.addView(tagView);

                    Space space = new Space(this);
                    space.setMinimumWidth(5);
                    tagsHolder.addView(space);
                }
            } else {
                tagsHolder.setVisibility(View.GONE);
            }

            String itemMeaning = jsonObject.getString("m");
            TextView meaning = findViewById(R.id.meaning);
            meaning.setText(itemMeaning);

            TableLayout components = findViewById(R.id.components);
            JSONArray comps = jsonObject.optJSONArray("c");
            if (comps != null && comps.length() > 0) {
                for (int i = 0; i < comps.length(); i++) {
                    JSONObject comp = comps.getJSONObject(i);
                    final String compString = !comp.optString("k", "").equals("null") ? comp.optString("k") : "";

                    TableRow row = new TableRow(this);
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra(MainActivity.KEYWORD_EXTRA, compString);
                            startActivity(intent);
                        }
                    });
                    components.addView(row);

                    TextView compKanji = new TextView(this);
                    compKanji.setText(compString);
                    compKanji.setTextAppearance(this, R.style.link);
                    row.addView(compKanji);

                    Space space = new Space(this);
                    space.setMinimumWidth(10);
                    row.addView(space);

                    TextView compMeaning = new TextView(this);
                    compMeaning.setText(comp.optString("m", ""));
                    compMeaning.setTextAppearance(this, R.style.component_label);
                    row.addView(compMeaning);
                }
            } else {
                components.setVisibility(View.GONE);
            }

            TextView description = findViewById(R.id.description);
            String descr = jsonObject.optString("d");
            if (descr != null && !descr.isEmpty()) {
                description.setText(formatHtmlString(descr));
            } else {
                description.setVisibility(View.GONE);
            }

            TextView onyomi = findViewById(R.id.onyomi);
            JSONArray on = jsonObject.optJSONArray("on");
            if (Utils.isNotEmpty(on)) {
                onyomi.setText(Utils.join(on));
            } else {
                TextView onyomiLabel = findViewById(R.id.onyomi_label);

                onyomiLabel.setVisibility(View.GONE);
                onyomi.setVisibility(View.GONE);
            }

            TextView mnemonic = findViewById(R.id.mnemonic);
            String mnemo = jsonObject.optString("mn");
            if (mnemo != null && !mnemo.isEmpty()) {
                mnemonic.setText(formatHtmlString(mnemo));
            } else {
                TextView mnemonicLabel = findViewById(R.id.mnemonic_label);

                mnemonicLabel.setVisibility(View.GONE);
                mnemonic.setVisibility(View.GONE);
            }

            TableLayout kunyomi = findViewById(R.id.kunyomi);
            JSONArray kun = jsonObject.optJSONArray("kun");
            if (Utils.isNotEmpty(kun)) {
                for (int i = 0; i < kun.length(); i++) {
                    TableRow row = new TableRow(this);
                    kunyomi.addView(row);

                    TableRow row2 = new TableRow(this);
                    kunyomi.addView(row2);

                    JSONObject kunReading = kun.getJSONObject(i);

                    TextView preParticle = new TextView(this);
                    preParticle.setText(kunReading.optString("preParticle", ""));
                    preParticle.setTextAppearance(this, R.style.particle);
                    row.addView(preParticle);

                    TextView kreading = new TextView(this);
                    kreading.setText(kunReading.optString("r", ""));
                    kreading.setTextAppearance(this, R.style.reading);
                    row.addView(kreading);

                    TextView kokurigana = new TextView(this);
                    kokurigana.setText(kunReading.optString("o", ""));
                    row.addView(kokurigana);

                    TextView postParticle = new TextView(this);
                    postParticle.setText(kunReading.optString("postParticle", ""));
                    postParticle.setTextAppearance(this, R.style.particle);
                    row.addView(postParticle);

                    TextView kunMeaning = new TextView(this);
                    kunMeaning.setText(kunReading.optString("m", ""));
                    row.addView(kunMeaning);

                    JSONArray ktags = kunReading.optJSONArray("t");
                    if (ktags != null) {
                        LinearLayout ktagsHolder = new LinearLayout(this);

                        for (int j = 0; j < ktags.length(); j++) {
                            String tag = ktags.getString(j);

                            TextView tagView = new TextView(this);
                            tagView.setText(tag);
                            tagView.setTextColor(Color.WHITE);
                            tagView.setTextAppearance(this, R.style.tag);
                            tagView.setBackgroundResource(R.drawable.rounded_corners);
                            ((GradientDrawable) tagView.getBackground()).setColor(Color.parseColor("#2d6987"));

                            ktagsHolder.addView(tagView);

                            Space space = new Space(this);
                            space.setMinimumWidth(5);
                            ktagsHolder.addView(space);
                        }

                        TableRow tagsRow = new TableRow(this);
                        kunyomi.addView(tagsRow);
                        tagsRow.addView(new TextView(this));
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                        layoutParams.span = 4;
                        tagsRow.addView(ktagsHolder, layoutParams);
                    }

                    row2.addView(new TextView(this));

                    TextView kunDescription = new TextView(this);
                    kunDescription.setText(kunReading.optString("d", ""));
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                    layoutParams.span = 4;
                    layoutParams.weight = 1;
                    row2.addView(kunDescription, layoutParams);
                }
            } else {
                TextView kunyomiLabel = findViewById(R.id.kunyomi_label);

                kunyomiLabel.setVisibility(View.GONE);
                kunyomi.setVisibility(View.GONE);
            }

            GridLayout usedInList = findViewById(R.id.usedin);
            JSONArray usedIn = jsonObject.optJSONArray("ui");
            if (usedIn != null && usedIn.length() > 0) {
                for (int i = 0; i < usedIn.length(); i++) {
                    JSONObject usedObject = usedIn.optJSONObject(i);
                    if (usedObject != null) {
                        final String kanji = usedObject.optString("k");
                        if (kanji != null && !kanji.isEmpty() && !kanji.equals("null")) {
                            TextView usedInKanji = new TextView(this);
                            usedInKanji.setText(kanji);
                            usedInKanji.setTextAppearance(this, R.style.link);
                            usedInKanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.putExtra(MainActivity.KEYWORD_EXTRA, kanji);
                                    startActivity(intent);
                                }
                            });

                            usedInList.addView(usedInKanji);
                        }
                    }
                }
            } else {
                TextView usedInLabel = findViewById(R.id.usedin_label);

                usedInLabel.setVisibility(View.GONE);
                usedInList.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Spanned formatHtmlString(String string) {
        String s = string
                .replaceAll("<b>", "<font color=\\\"#f89406\\\">")
                .replaceAll("</b>", "</font>")

                .replaceAll("<i>", "<font color=\\\"#46a546\\\">")
                .replaceAll("</i>", "</font>")

                .replaceAll("<ins>", "<font color=\\\"#9d261d\\\">")
                .replaceAll("</ins>", "</font>")
                ;

        return Html.fromHtml(s);
    }

    private void showPopupMenu(final View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.label_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence text = ((TextView) v).getText();
                switch (item.getItemId()) {
                    case R.id.menu_item_search:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra(MainActivity.KEYWORD_EXTRA, text);
                        startActivity(intent);
                        break;

                    case R.id.menu_item_jisho:
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("https://jisho.org/search/" + Uri.encode(text.toString())));
                        startActivity(i);
                        break;

                    case R.id.menu_item_copy:
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(text, text);
                        clipboard.setPrimaryClip(clip);
                        break;
                }

                return true;
            }
        });
        popupMenu.show();
    }
}
