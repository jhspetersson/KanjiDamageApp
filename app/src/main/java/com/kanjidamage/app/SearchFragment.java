package com.kanjidamage.app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SearchFragment extends Fragment implements View.OnClickListener {
    static final String KEYWORD_EXTRA = "keyword";

    private RecyclerView cards;

    private String keyword;

    private OnOpenItemListener listener;

    public static SearchFragment newInstance(String keyword, OnOpenItemListener listener) {
        SearchFragment fragment = new SearchFragment();
        fragment.keyword = keyword;
        fragment.listener = listener;

        return fragment;
    }

    private SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        EditText search = view.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcherHelper() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = String.valueOf(s);
                updateSearchResults(keyword);
            }
        });
        search.requestFocus();

        cards = view.findViewById(R.id.cards);
        cards.setLayoutManager(new LinearLayoutManager(getContext()));

        try {
            /*
            CharSequence keyword = getActivity().getIntent().getStringExtra(KEYWORD_EXTRA);

            if (keyword == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    keyword = getActivity().getIntent().getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
                }
            }

            if (keyword != null) {
                search.setText(keyword.toString());
            }
            */
            if (Utils.isNotEmpty(keyword)) {
                search.setText(keyword.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private void updateSearchResults(String keyword) {
        DataAdapter adapter = new DataAdapter(filter(keyword), this);
        cards.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        listener.onOpenItem(v.getTag(R.id.label).toString(), v.getTag(R.id.description).toString());
    }

    private List<Map<String, String>> filter(String keyword) {
        List<Map<String, String>> result = new ArrayList<>();

        keyword = keyword.trim();
        if (!keyword.isEmpty()) {
            for (Map<String, String> row : Data.data) {
                if (row.get("label").contains(keyword) || row.get("comment").contains(keyword) ||
                        (row.containsKey("onyomi") && row.get("onyomi").contains(keyword))) {
                    result.add(row);
                }
            }
        }

        return result;
    }

    public interface OnOpenItemListener {
        void onOpenItem(String title, String jsonString);
    }
}
