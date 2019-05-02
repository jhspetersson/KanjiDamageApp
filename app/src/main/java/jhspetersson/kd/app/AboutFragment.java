package jhspetersson.kd.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AboutFragment extends Fragment {
    public AboutFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        final TextView kdLink = view.findViewById(R.id.kd_url);
        kdLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = kdLink.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        final TextView authorLink = view.findViewById(R.id.author_url);
        authorLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = authorLink.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        return view;
    }
}
