package com.kanjidamage.app;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        final TextView kdLink = findViewById(R.id.kd_url);
        kdLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = kdLink.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        final TextView authorLink = findViewById(R.id.author_url);
        authorLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = authorLink.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }
}
