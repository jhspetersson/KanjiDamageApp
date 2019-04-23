package com.kanjidamage.app;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class DataRow extends RecyclerView.ViewHolder implements View.OnClickListener {

    private String json;

    private TextView label;
    private TextView description;
    private TextView comment;
    private TextView onyomi;

    public DataRow(View itemView) {
        super(itemView);
        itemView.findViewById(R.id.card).setOnClickListener(this);

        label = itemView.findViewById(R.id.label);
        description = itemView.findViewById(R.id.description);
        comment = itemView.findViewById(R.id.comment);
        onyomi = itemView.findViewById(R.id.onyomi);
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setDescription(String text) {
        description.setText(text);
    }

    public void setComment(String text) {
        comment.setText(text);
    }

    public void setOnyomi(String text) {
        onyomi.setText(text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ItemActivity.class);
        intent.putExtra("json", json);
        v.getContext().startActivity(intent);
    }
}
