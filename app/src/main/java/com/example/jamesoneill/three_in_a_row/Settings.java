package com.example.jamesoneill.three_in_a_row;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TableLayout;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabHost host = findViewById(R.id.tabHost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Default Color");
        spec.setContent(R.id.default_color);
        spec.setIndicator("Default Tile Color");
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

    protected void test(View view){
        SeekBar color1Red = findViewById(R.id.color_1_red_seek);
        SeekBar color1Green = findViewById(R.id.color_1_green_seek);
        SeekBar color1Blue = findViewById(R.id.color_1_blue_seek);

        SeekBar color2Red = findViewById(R.id.color_2_red_seek);
        SeekBar color2Green = findViewById(R.id.color_2_green_seek);
        SeekBar color2Blue = findViewById(R.id.color_2_blue_seek);

        SeekBar defaultRed = findViewById(R.id.default_red_seek);
        SeekBar defaultGreen = findViewById(R.id.default_green_seek);
        SeekBar defaultBlue = findViewById(R.id.default_blue_seek);

        Config.setDefaultColor(defaultRed.getProgress(), defaultGreen.getProgress(), defaultBlue.getProgress());
        Config.setFirstColor(color1Red.getProgress(), color1Green.getProgress(), color1Blue.getProgress());
        Config.setSecondColor(color2Red.getProgress(), color2Green.getProgress(), color2Blue.getProgress());
    }

}
