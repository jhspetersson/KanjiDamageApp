package jhspetersson.kd.app;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

public class DataAdapter extends RecyclerView.Adapter<DataRow> {
    private List<Map<String, String>> data;
    private View.OnClickListener callback;

    public DataAdapter(List<Map<String, String>> data, View.OnClickListener callback) {
        this.data = data;
        this.callback = callback;
    }

    @NonNull
    @Override
    public DataRow onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card, parent, false);

        return new DataRow(view, callback);
    }

    @Override
    public void onBindViewHolder(@NonNull DataRow holder, int position) {
        Map<String, String> rowData = data.get(position);
        holder.setJson(rowData.get("json"));
        holder.setLabel(rowData.get("label"));
        holder.setDescription(rowData.get("description"));

        String comment = rowData.get("comment");

        String onyomi = rowData.get("onyomi");
        if (onyomi != null && !onyomi.isEmpty()) {
            if (!comment.isEmpty()) {
                comment += " / ";
            }
            comment += "<font color=\"#f89406\">" + onyomi + "</font>";
        }

        holder.setComment(comment);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
