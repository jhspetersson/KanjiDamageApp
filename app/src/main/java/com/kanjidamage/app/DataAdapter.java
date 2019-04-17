package com.kanjidamage.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataRow> {

    private List<Map<String, String>> data;

    public DataAdapter(List<Map<String, String>> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public DataRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card, parent, false);

        return new DataRow(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DataRow holder, int position) {
        Map<String, String> rowData = data.get(position);
        holder.setLabel(rowData.get("label"));
        holder.setDescription(rowData.get("description"));
        holder.setComment(rowData.get("comment"));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
