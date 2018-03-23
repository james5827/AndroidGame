package com.example.jamesoneill.three_in_a_row;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TabHost;

import static android.content.ContentValues.TAG;

public class Settings extends AppCompatActivity {

    private View preview;
    private int redBarValue = 0;
    private int greenBarValue = 0;
    private int blueBarValue = 0;

    private SeekBar defaultRed;
    private SeekBar defaultGreen;
    private SeekBar defaultBlue;

    private SeekBar color1Red;
    private SeekBar color1Green;
    private SeekBar color1Blue;

    private SeekBar color2Red;
    private SeekBar color2Green;
    private SeekBar color2Blue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inflateTabs();

        preview = findViewById(R.id.colorPreview);

        initializeSeekBars();
    }

    private void inflateTabs()
    {
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

        host.setOnTabChangedListener(this::onTabChange);
    }

    private void onTabChange(String tabId){
        int color = 0;

        switch(tabId){
            case "Default Color":
               color = Color.argb(255, defaultRed.getProgress(), defaultGreen.getProgress(), defaultBlue.getProgress());
                break;
            case "Color One":
                color = Color.argb(255, color1Red.getProgress(), color1Green.getProgress(), color1Blue.getProgress());
                break;
            case "Color Two":
                color = Color.argb(255, color2Red.getProgress(), color2Green.getProgress(), color2Blue.getProgress());
                break;
        }

        preview.setBackgroundColor(color);
    }

    private void initializeSeekBars()
    {
        int defaultColor = Config.getDefaultColor();

        int defaultRedValue = Color.red(defaultColor);
        int defaultGreenValue = Color.green(defaultColor);
        int defaultBlueValue = Color.blue(defaultColor);

        defaultRed = findViewById(R.id.default_red_seek);
        defaultGreen = findViewById(R.id.default_green_seek);
        defaultBlue = findViewById(R.id.default_blue_seek);

        defaultRed.setProgress(defaultRedValue);
        defaultGreen.setProgress(defaultGreenValue);
        defaultBlue.setProgress(defaultBlueValue);

        preview.setBackgroundColor(defaultColor);

        int firstTileColor = Config.getFirstColor();

        int firstTileRedValue = Color.red(firstTileColor);
        int firstTileGreenValue = Color.green(firstTileColor);
        int firstTileBlueValue = Color.blue(firstTileColor);

        color1Red = findViewById(R.id.color_1_red_seek);
        color1Green = findViewById(R.id.color_1_green_seek);
        color1Blue = findViewById(R.id.color_1_blue_seek);

        color1Red.setProgress(firstTileRedValue);
        color1Green.setProgress(firstTileGreenValue);
        color1Blue.setProgress(firstTileBlueValue);

        int secondTileColor = Config.getSecondColor();

        int secondTileRedValue = Color.red(secondTileColor);
        int secondTileGreenValue = Color.green(secondTileColor);
        int secondTileBlueValue = Color.blue(secondTileColor);

        color2Red = findViewById(R.id.color_2_red_seek);
        color2Green = findViewById(R.id.color_2_green_seek);
        color2Blue = findViewById(R.id.color_2_blue_seek);

        color2Red.setProgress(secondTileRedValue);
        color2Green.setProgress(secondTileGreenValue);
        color2Blue.setProgress(secondTileBlueValue);

        defaultRed.setOnSeekBarChangeListener(new redSeekBarChange());
        defaultGreen.setOnSeekBarChangeListener(new GreenSeekBarChange());
        defaultBlue.setOnSeekBarChangeListener(new BlueSeekBarChange());

        color1Red.setOnSeekBarChangeListener(new redSeekBarChange());
        color1Green.setOnSeekBarChangeListener(new GreenSeekBarChange());
        color1Blue.setOnSeekBarChangeListener(new BlueSeekBarChange());

        color2Red.setOnSeekBarChangeListener(new redSeekBarChange());
        color2Green.setOnSeekBarChangeListener(new GreenSeekBarChange());
        color2Blue.setOnSeekBarChangeListener(new BlueSeekBarChange());
    }


    private class redSeekBarChange implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b)
                preview.setBackgroundColor(Color.argb(255, i, greenBarValue, blueBarValue));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            redBarValue = seekBar.getProgress();
        }
    }

    private class GreenSeekBarChange implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b)
                preview.setBackgroundColor(Color.argb(255, redBarValue, i, blueBarValue));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            greenBarValue = seekBar.getProgress();
        }
    }

    private class BlueSeekBarChange implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if(b)
                preview.setBackgroundColor(Color.argb(255, redBarValue, greenBarValue, i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            blueBarValue = seekBar.getProgress();
        }
    }
}


