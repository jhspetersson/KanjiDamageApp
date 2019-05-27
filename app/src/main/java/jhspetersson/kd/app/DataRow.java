package jhspetersson.kd.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;

public class DataRow extends RecyclerView.ViewHolder {
    private CardView card;
    private TextView label;
    private TextView description;
    private TextView comment;
    private LinearLayout tagsHolder;

    public DataRow(View itemView, View.OnClickListener callback) {
        super(itemView);

        card = itemView.findViewById(R.id.card);
        card.setOnClickListener(callback);

        label = itemView.findViewById(R.id.label);
        description = itemView.findViewById(R.id.description);
        comment = itemView.findViewById(R.id.comment);
        tagsHolder = itemView.findViewById(R.id.tags_holder);
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

    public void setTags(String[] tags, Context context) {
        tagsHolder.removeAllViews();

        for (String tag : tags) {
            TextView tagView = new TextView(context);
            tagView.setText(tag);
            tagView.setTextColor(Color.WHITE);
            tagView.setTextAppearance(context, R.style.tag);
            tagView.setBackgroundResource(R.drawable.rounded_corners);
            ((GradientDrawable) tagView.getBackground()).setColor(Color.parseColor("#2d6987"));

            tagsHolder.addView(tagView);

            Space space = new Space(context);
            space.setMinimumWidth(5);
            tagsHolder.addView(space);
        }
    }
}
