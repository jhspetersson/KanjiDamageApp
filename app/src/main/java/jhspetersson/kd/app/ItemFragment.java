package jhspetersson.kd.app;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


public class ItemFragment extends Fragment {

    private String json;

    ItemFragment.OnSearchListener listener;

    public static ItemFragment newInstance(String json, ItemFragment.OnSearchListener listener) {
        ItemFragment fragment = new ItemFragment();
        fragment.json = json;
        fragment.listener = listener;

        return fragment;
    }

    private ItemFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        try {
            JSONObject jsonObject = new JSONObject(json);
            String labelString = jsonObject.getString("k");
            JSONObject okurigana = jsonObject.optJSONObject("o");
            if (okurigana != null) {
                labelString = okurigana.optString("pre", "") + labelString + okurigana.optString("post", "");
            }

            TextView label = view.findViewById(R.id.label);
            label.setText(labelString);
            label.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });

            RatingBar usefulness = view.findViewById(R.id.usefulness);
            double use = jsonObject.optDouble("u", -1);
            if (use >= 0) {
                usefulness.setRating((float) use);
            } else {
                usefulness.setVisibility(View.GONE);
            }

            TextView reading = view.findViewById(R.id.reading);
            String read = jsonObject.optString("r");
            if (read != null && !read.isEmpty()) {
                if (okurigana != null) {
                    read = okurigana.optString("pre", "") + read + okurigana.optString("post", "");
                }
                reading.setText(read);
            } else {
                reading.setVisibility(View.GONE);
            }

            LinearLayout tagsHolder = view.findViewById(R.id.tags_holder);
            JSONArray tags = jsonObject.optJSONArray("t");
            if (tags != null) {
                for (int i = 0; i < tags.length(); i++) {
                    String tag = tags.getString(i);

                    TextView tagView = new TextView(getContext());
                    tagView.setText(tag);
                    tagView.setTextColor(Color.WHITE);
                    tagView.setTextAppearance(getContext(), R.style.tag);
                    tagView.setBackgroundResource(R.drawable.rounded_corners);
                    ((GradientDrawable) tagView.getBackground()).setColor(Color.parseColor("#2d6987"));

                    tagsHolder.addView(tagView);

                    Space space = new Space(getContext());
                    space.setMinimumWidth(5);
                    tagsHolder.addView(space);
                }
            } else {
                tagsHolder.setVisibility(View.GONE);
            }

            String itemMeaning = jsonObject.getString("m");
            TextView meaning = view.findViewById(R.id.meaning);
            meaning.setText(itemMeaning);

            TableLayout components = view.findViewById(R.id.components);
            JSONArray comps = jsonObject.optJSONArray("c");
            if (comps != null && comps.length() > 0) {
                for (int i = 0; i < comps.length(); i++) {
                    JSONObject comp = comps.getJSONObject(i);
                    final String compString = !comp.optString("k", "").equals("null") ? comp.optString("k") : "";

                    TableRow row = new TableRow(getContext());
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            listener.onSearch(compString);
                        }
                    });
                    components.addView(row);

                    TextView compKanji = new TextView(getContext());
                    compKanji.setText(compString);
                    compKanji.setTextAppearance(getContext(), R.style.link);
                    row.addView(compKanji);

                    Space space = new Space(getContext());
                    space.setMinimumWidth(10);
                    row.addView(space);

                    TextView compMeaning = new TextView(getContext());
                    compMeaning.setText(comp.optString("m", ""));
                    compMeaning.setTextAppearance(getContext(), R.style.component_label);
                    row.addView(compMeaning);
                }
            } else {
                components.setVisibility(View.GONE);
            }

            TextView description = view.findViewById(R.id.description);
            String descr = jsonObject.optString("d");
            if (descr != null && !descr.isEmpty()) {
                description.setText(formatHtmlString(descr));
            } else {
                description.setVisibility(View.GONE);
            }

            TextView onyomi = view.findViewById(R.id.onyomi);
            JSONArray on = jsonObject.optJSONArray("on");
            if (Utils.isNotEmpty(on)) {
                onyomi.setText(Utils.join(on));
            } else {
                TextView onyomiLabel = view.findViewById(R.id.onyomi_label);

                onyomiLabel.setVisibility(View.GONE);
                onyomi.setVisibility(View.GONE);
            }

            TextView mnemonic = view.findViewById(R.id.mnemonic);
            String mnemo = jsonObject.optString("mn");
            if (mnemo != null && !mnemo.isEmpty()) {
                mnemonic.setText(formatHtmlString(mnemo));
            } else {
                TextView mnemonicLabel = view.findViewById(R.id.mnemonic_label);

                mnemonicLabel.setVisibility(View.GONE);
                mnemonic.setVisibility(View.GONE);
            }

            TableLayout kunyomi = view.findViewById(R.id.kunyomi);
            JSONArray kun = jsonObject.optJSONArray("kun");
            if (Utils.isNotEmpty(kun)) {
                for (int i = 0; i < kun.length(); i++) {
                    TableRow row = new TableRow(getContext());
                    kunyomi.addView(row);

                    TableRow row2 = new TableRow(getContext());
                    kunyomi.addView(row2);

                    JSONObject kunReading = kun.getJSONObject(i);

                    TextView preParticle = new TextView(getContext());
                    preParticle.setText(kunReading.optString("preParticle", ""));
                    preParticle.setTextAppearance(getContext(), R.style.particle);
                    row.addView(preParticle);

                    TextView kreading = new TextView(getContext());
                    kreading.setText(kunReading.optString("r", ""));
                    kreading.setTextAppearance(getContext(), R.style.reading);
                    row.addView(kreading);

                    TextView kokurigana = new TextView(getContext());
                    kokurigana.setText(kunReading.optString("o", ""));
                    row.addView(kokurigana);

                    TextView postParticle = new TextView(getContext());
                    postParticle.setText(kunReading.optString("postParticle", ""));
                    postParticle.setTextAppearance(getContext(), R.style.particle);
                    row.addView(postParticle);

                    TextView kunMeaning = new TextView(getContext());
                    kunMeaning.setText(kunReading.optString("m", ""));
                    kunMeaning.setTextIsSelectable(true);
                    row.addView(kunMeaning);

                    JSONArray ktags = kunReading.optJSONArray("t");
                    if (ktags != null) {
                        LinearLayout ktagsHolder = new LinearLayout(getContext());

                        for (int j = 0; j < ktags.length(); j++) {
                            String tag = ktags.getString(j);

                            TextView tagView = new TextView(getContext());
                            tagView.setText(tag);
                            tagView.setTextColor(Color.WHITE);
                            tagView.setTextAppearance(getContext(), R.style.tag);
                            tagView.setBackgroundResource(R.drawable.rounded_corners);
                            ((GradientDrawable) tagView.getBackground()).setColor(Color.parseColor("#2d6987"));

                            ktagsHolder.addView(tagView);

                            Space space = new Space(getContext());
                            space.setMinimumWidth(5);
                            ktagsHolder.addView(space);
                        }

                        TableRow tagsRow = new TableRow(getContext());
                        kunyomi.addView(tagsRow);
                        tagsRow.addView(new TextView(getContext()));
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                        layoutParams.span = 4;
                        tagsRow.addView(ktagsHolder, layoutParams);
                    }

                    row2.addView(new TextView(getContext()));

                    TextView kunDescription = new TextView(getContext());
                    kunDescription.setText(kunReading.optString("d", ""));
                    kunDescription.setTextIsSelectable(true);
                    TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                    layoutParams.span = 4;
                    layoutParams.weight = 1;
                    row2.addView(kunDescription, layoutParams);
                }
            } else {
                TextView kunyomiLabel = view.findViewById(R.id.kunyomi_label);

                kunyomiLabel.setVisibility(View.GONE);
                kunyomi.setVisibility(View.GONE);
            }

            GridLayout usedInList = view.findViewById(R.id.usedin);
            JSONArray usedIn = jsonObject.optJSONArray("ui");
            if (usedIn != null && usedIn.length() > 0) {
                for (int i = 0; i < usedIn.length(); i++) {
                    JSONObject usedObject = usedIn.optJSONObject(i);
                    if (usedObject != null) {
                        final String kanji = usedObject.optString("k");
                        if (kanji != null && !kanji.isEmpty() && !kanji.equals("null")) {
                            TextView usedInKanji = new TextView(getContext());
                            usedInKanji.setText(kanji);
                            usedInKanji.setTextAppearance(getContext(), R.style.link);
                            usedInKanji.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    listener.onSearch(kanji);
                                }
                            });

                            usedInList.addView(usedInKanji);

                            Space space = new Space(getContext());
                            space.setMinimumWidth(15);

                            usedInList.addView(space);
                        }
                    }
                }
            } else {
                TextView usedInLabel = view.findViewById(R.id.usedin_label);

                usedInLabel.setVisibility(View.GONE);
                usedInList.setVisibility(View.GONE);
            }

            TableLayout lookalikes = view.findViewById(R.id.lookalikes);
            JSONArray las = jsonObject.optJSONArray("las");
            if (las != null && las.length() > 0) {
                TableRow.LayoutParams marginParams = new TableRow.LayoutParams();
                marginParams.rightMargin = 15;

                for (int i = 0; i < las.length(); i++) {
                    JSONObject lasMember = las.getJSONObject(i);

                    JSONArray lat = lasMember.getJSONArray("lat");
                    for (int j = 0; j < lat.length(); j++) {
                        JSONObject latMember = lat.getJSONObject(j);
                        final String kanjiString = latMember.optString("k", "");

                        TableRow row = new TableRow(getContext());
                        row.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                listener.onSearch(kanjiString);
                            }
                        });
                        lookalikes.addView(row);

                        TextView laKanji = new TextView(getContext());
                        laKanji.setText(kanjiString);
                        laKanji.setTextAppearance(getContext(), R.style.link);
                        row.addView(laKanji, marginParams);

                        TextView laMeaning = new TextView(getContext());
                        laMeaning.setText(latMember.optString("m", ""));
                        laMeaning.setTextAppearance(getContext(), R.style.component_label);
                        row.addView(laMeaning, marginParams);

                        TextView laHint = new TextView(getContext());
                        laHint.setText(latMember.optString("hint", ""));
                        row.addView(laHint, marginParams);

                        TextView laRadical = new TextView(getContext());
                        laRadical.setText(latMember.optString("radical", ""));
                        row.addView(laRadical);
                    }

                    JSONArray lam = lasMember.optJSONArray("lam");
                    if (Utils.isNotEmpty(lam)) {
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams();
                        layoutParams.span = 4;
                        layoutParams.weight = 1;

                        for (int j = 0; j < lam.length(); j++) {
                            TableRow row = new TableRow(getContext());
                            lookalikes.addView(row);

                            TextView laMnemonic = new TextView(getContext());
                            laMnemonic.setText(Html.fromHtml(lam.getString(j)));
                            laMnemonic.setTextIsSelectable(true);
                            row.addView(laMnemonic, layoutParams);
                        }
                    }
                }
            } else {
                TextView lookalikesLabel = view.findViewById(R.id.lookalikes_label);

                lookalikesLabel.setVisibility(View.GONE);
                lookalikes.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
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
        PopupMenu popupMenu = new PopupMenu(getContext(), v);
        popupMenu.inflate(R.menu.label_menu);

        if (!isKanjiStudyInstalled() || Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            popupMenu.getMenu().removeItem(R.id.menu_item_kanjistudy);
        }

        if (!isTakobotoInstalled()) {
            popupMenu.getMenu().removeItem(R.id.menu_item_takoboto);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CharSequence text = ((TextView) v).getText();
                switch (item.getItemId()) {
                    case R.id.menu_item_search:
                        listener.onSearch(text.toString());
                        break;

                    case R.id.menu_item_copy:
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText(text, text);
                        clipboard.setPrimaryClip(clip);
                        break;

                    case R.id.menu_item_jisho:
                        Utils.openUrl("https://jisho.org/search/" + Uri.encode(text.toString()), getContext());
                        break;

                    case R.id.menu_item_kanjistudy:
                        Intent ks = new Intent(Intent.ACTION_VIEW);
                        ks.setData(Uri.parse("kanjistudy://search"));
                        ks.putExtra(Intent.EXTRA_PROCESS_TEXT, text.toString());
                        ks.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(ks);
                        break;

                    case R.id.menu_item_takoboto:
                        Intent takoboto = new Intent("jp.takoboto.SEARCH");
                        takoboto.putExtra("q", text.toString());
                        takoboto.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(takoboto);
                        break;
                }

                return true;
            }
        });
        popupMenu.show();
    }

    private boolean isKanjiStudyInstalled() {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("kanjistudy://search"));

        return Utils.isIntentSupported(intent, getActivity());
    }

    private boolean isTakobotoInstalled() {
        final Intent intent = new Intent("jp.takoboto.SEARCH");

        return Utils.isIntentSupported(intent, getActivity());
    }

    public interface OnSearchListener {
        void onSearch(String keyword);
    }
}
