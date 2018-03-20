package com.example.jamesoneill.three_in_a_row;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TabHost;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TabHost host = findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Default Color");
        spec.setContent(R.id.default_color);
        spec.setIndicator("Default Color");
        host.addTab(spec);

        spec = host.newTabSpec("Color One");
        spec.setContent(R.id.color_1);
        spec.setIndicator("Color One");
        host.addTab(spec);

        spec = host.newTabSpec("Color Two");
        spec.setContent(R.id.color_2);
        spec.setIndicator("Color Two");
        host.addTab(spec);
    }

}