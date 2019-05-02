package com.kanjidamage.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Space;
import android.widget.TextView;


public class KanjiListFragment extends Fragment {

    ItemFragment.OnSearchListener listener;

    public static KanjiListFragment newInstance(ItemFragment.OnSearchListener listener) {
        KanjiListFragment fragment = new KanjiListFragment();
        fragment.listener = listener;

        return fragment;
    }

    private KanjiListFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kanji_list, container, false);

        GridLayout kanjiGrid = view.findViewById(R.id.kanji_grid);

        for (int i = 0; i < Data.kanji.size(); i++) {
            final String kanji = Data.kanji.get(i);

            TextView kanjiView = new TextView(getContext());
            kanjiView.setText(kanji);
            kanjiView.setTextAppearance(getContext(), R.style.link);
            kanjiView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onSearch(kanji);
                }
            });
            kanjiGrid.addView(kanjiView);

            Space space = new Space(getContext());
            space.setMinimumWidth(15);
            kanjiGrid.addView(space);
        }


        return view;
    }

}
