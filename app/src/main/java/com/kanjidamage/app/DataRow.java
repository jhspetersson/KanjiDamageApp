package com.kanjidamage.app;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

public class DataRow extends RecyclerView.ViewHolder {
    private CardView card;
    private TextView label;
    private TextView description;
    private TextView comment;

    public DataRow(View itemView, View.OnClickListener callback) {
        super(itemView);

        card = itemView.findViewById(R.id.card);
        card.setOnClickListener(callback);

        label = itemView.findViewById(R.id.label);
        description = itemView.findViewById(R.id.description);
        comment = itemView.findViewById(R.id.comment);
    }

    public void setJson(String json) {
        card.setTag(R.id.description, json);
    }

    public void setLabel(String text) {
        label.setText(text);
        card.setTag(R.id.label, text);
    }

    public void setDescription(String text) {
        description.setText(text);
    }

    public void setComment(String text) {
        comment.setText(Html.fromHtml(text));
    }
}
