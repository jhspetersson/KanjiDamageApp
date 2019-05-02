package jhspetersson.kd.app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Data.DataLoadCallback, SearchFragment.OnOpenItemListener, ItemFragment.OnSearchListener {

    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        loadData();

        if (!Data.data.isEmpty()) {
            String keyword = getDefaultKeyword();
            navigateFirstMenuItem(keyword);
        }
    }

    private String getDefaultKeyword() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CharSequence extra = getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
            if (extra != null) {
                return extra.toString();
            }
        }

        return "";
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FrameLayout container = findViewById(R.id.container);
            if (container.getChildCount() == 1) {
                super.onBackPressed();

                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry backStackEntry = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1);
                    String title = backStackEntry.getName();
                    setTitle(title);
                }

                if (container.getChildCount() == 0) {
                    finish();
                }
            } else if (container.getChildCount() == 0) {
                navigateFirstMenuItem("");
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Fragment fragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_search) {
            fragment = SearchFragment.newInstance("", this);
        } else if (id == R.id.nav_kanji) {
            fragment = KanjiListFragment.newInstance(this);
        } else if (id == R.id.nav_about) {
            fragment = new AboutFragment();
        } else if (id == R.id.nav_kanjidamage) {
            Utils.openUrl("http://www.kanjidamage.com", this);
        } else if (id == R.id.nav_github) {
            Utils.openUrl("https://github.com/jhspetersson/KanjiDamageApp", this);
        }

        if (fragment != null) {
            navigateMenuItem(item, fragment);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void navigateMenuItem(MenuItem item, Fragment fragment) {
        item.setChecked(true);
        changeFragment(fragment, item.getTitle().toString());
    }

    private void changeFragment(Fragment fragment, String title) {
        setTitle(title);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(title)
                .commit();
    }

    private void loadData() {
        if (Data.data.isEmpty()) {
            setTitle(R.string.loading);
            LoadDataTask task = new LoadDataTask();
            task.callback = this;
            task.execute(this);
        }
    }

    @Override
    public void onDataLoaded() {
        navigateFirstMenuItem(getDefaultKeyword());
    }

    private void navigateFirstMenuItem(String keyword) {
        MenuItem searchMenuItem = navigationView.getMenu().getItem(0);

        try {
            navigateMenuItem(searchMenuItem, SearchFragment.newInstance(keyword, this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOpenItem(String title, String jsonString) {
        ItemFragment fragment = ItemFragment.newInstance(jsonString, this);
        changeFragment(fragment, title);
    }

    @Override
    public void onSearch(String keyword) {
        navigateFirstMenuItem(keyword);
    }

    private static class LoadDataTask extends AsyncTask<Context, Void, Void> {
        private Data.DataLoadCallback callback;

        @Override
        protected Void doInBackground(Context... params) {
            JSONObject json = loadJSONFromAsset(params[0]);
            List<Map<String, String>> data = new ArrayList<>();

            try {
                JSONArray kanjis = json.getJSONArray("kanji");
                for (int i = 0; i < kanjis.length(); i++) {
                    JSONObject kanji = kanjis.getJSONObject(i);
                    Map<String, String> row = new HashMap<>();
                    row.put("json", kanji.toString());
                    row.put("label", kanji.getString("k"));
                    row.put("description", kanji.getString("m"));

                    List<String> kun = new ArrayList<>();
                    JSONArray kunyomis = kanji.optJSONArray("kun");
                    if (Utils.isNotEmpty(kunyomis)) {
                        for (int j = 0; j < kunyomis.length(); j++) {
                            JSONObject kunyomi = kunyomis.getJSONObject(j);
                            String reading = kunyomi.getString("r");
                            String okurigana = kunyomi.optString("o");
                            if (okurigana == null || okurigana.isEmpty()) {
                                kun.add(reading);
                            } else {
                                kun.add(reading + "." + okurigana);
                            }
                        }
                    }

                    List<String> on = new ArrayList<>();
                    JSONArray onyomis = kanji.optJSONArray("on");
                    if (onyomis != null) {
                        for (int j = 0; j < onyomis.length(); j++) {
                            on.add(onyomis.getString(j));
                        }
                    }

                    String comment = "";
                    if (!kun.isEmpty()) {
                        comment += Utils.join(kun);
                    }

                    row.put("comment", comment);

                    if (Utils.isNotEmpty(on)) {
                        row.put("onyomi", Utils.join(on));
                    }

                    data.add(row);

                    String k = kanji.getString("k");
                    if (Utils.isNotEmpty(k)) {
                        Data.kanji.add(k);
                    }
                }

                JSONArray jukugos = json.getJSONArray("jukugo");
                for (int i = 0; i < jukugos.length(); i++) {
                    JSONObject jukugo = jukugos.getJSONObject(i);
                    Map<String, String> row = new HashMap<>();
                    row.put("json", jukugo.toString());

                    String label = jukugo.getString("k");
                    JSONObject okurigana = jukugo.optJSONObject("o");
                    if (okurigana != null) {
                        label = okurigana.optString("pre", "") + label + okurigana.optString("post", "");
                    }

                    row.put("label", label);
                    row.put("description", jukugo.getString("m"));

                    String reading = jukugo.getString("r");
                    if (okurigana != null) {
                        reading = okurigana.optString("pre", "") + reading + okurigana.optString("post", "");
                    }

                    row.put("comment", reading);
                    data.add(row);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Data.data = data;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onDataLoaded();
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
