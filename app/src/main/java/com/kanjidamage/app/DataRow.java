package com.kanjidamage.app;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class DataRow extends RecyclerView.ViewHolder {

    private TextView label;
    private TextView description;

    public DataRow(View itemView) {
        super(itemView);

        label = itemView.findViewById(R.id.label);
        description = itemView.findViewById(R.id.description);
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setDescription(String text) {
        description.setText(text);
    }
}
