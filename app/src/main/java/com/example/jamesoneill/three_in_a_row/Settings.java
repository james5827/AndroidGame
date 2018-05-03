package com.example.jamesoneill.three_in_a_row;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Activity Class for the settings screen
 * that houses the inputs that update the config class
 */
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

    private byte currentTab;
    private final byte DEFAULT_TILE_TAB = 0;
    private final byte FIRST_TILE_TAB = 1;
    private final byte SECOND_TILE_TAB = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inflateTabs();

        preview = findViewById(R.id.colorPreview);

        initializeSeekBars();

        SeekBar gridSize = findViewById(R.id.gridSeek);
        TextView lblGridView = findViewById(R.id.lblGridSeek);
        gridSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                switch(i) {
                    case 0:
                        i = 4;
                        break;
                    case 1:
                        i = 5;
                        break;
                    case 2:
                        i = 6;
                        break;
                    case 3:
                        i = 7;
                        break;
                }
                lblGridView.setText(String.format(getString(R.string.grid_size_seek_bar), i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                byte colNumber = 0;
                switch (seekBar.getProgress()){
                    case 0:
                        colNumber = 4;
                        break;
                    case 1:
                        colNumber = 5;
                        break;
                    case 2:
                        colNumber = 6;
                        break;
                    case 3:
                        colNumber = 7;
                        break;
                }
                Log.i("WORK", "onStopTrackingTouch: " + colNumber);
                Config.setColNumbers(colNumber);
                Log.i("WORK", "onStopTrackingTouch: " + Config.getColNumbers());
            }
        });
        gridSize.setProgress(Config.getColNumbers() -4);
        lblGridView.setText(String.format(getString(R.string.grid_size_seek_bar), Config.getColNumbers()));
    }

    /**
     * Calls the Config save function in order to save the settings to file
     * on activity stop
     */
    @Override
    protected void onStop(){
        Config.saveSettings(this.getApplicationContext());
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        Intent i = new Intent();

        switch (id){
            case R.id.settings_menu_home:
                i = new Intent(this, Home.class);
                break;
            case R.id.settings_menu_play:
                i = new Intent(this, Play.class);
                break;
            case R.id.settings_menu_reset:
                resetSettings();
                i = null;
                break;
            case R.id.settings_menu_help:
                i = new Intent(this, Help.class);
                break;
        }

        if(i != null)
            startActivity(i);

        return super.onOptionsItemSelected(item);
    }

    /**
     * Reset all settings to default and save to SharedPreferences
     */
    private void resetSettings() {

    }

    /**
     * Create Tab View
     */
    private void inflateTabs() {
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

        currentTab = DEFAULT_TILE_TAB;
    }

    /**
     * Update the color seekBars and color preview onTabChange
     * @param tabId - the title of the tab
     */
    private void onTabChange(String tabId){
        int r = 0;
        int g = 0;
        int b =0;
        switch(tabId){
            case "Default Color":
                r = defaultRed.getProgress();
                g = defaultGreen.getProgress();
                b = defaultBlue.getProgress();
               currentTab = DEFAULT_TILE_TAB;
                break;
            case "Color One":
                r = color1Red.getProgress();
                g = color1Green.getProgress();
                b = color1Blue.getProgress();
                currentTab = FIRST_TILE_TAB;
                break;
            case "Color Two":
                r = color2Red.getProgress();
                g = color2Green.getProgress();
                b = color2Blue.getProgress();
                currentTab = SECOND_TILE_TAB;
                break;
        }
        redBarValue = r;
        greenBarValue = g;
        blueBarValue = b;
        preview.setBackgroundColor(Color.argb(255, r, g, b));
    }

    /**
     * Loads the current color values and presets the seek bars
     */
    private void initializeSeekBars()
    {
        int defaultColor = Config.getDefaultColor();

        int defaultRedValue = Color.red(defaultColor);
        int defaultGreenValue = Color.green(defaultColor);
        int defaultBlueValue = Color.blue(defaultColor);

        redBarValue = defaultRedValue;
        greenBarValue = defaultGreenValue;
        blueBarValue = defaultBlueValue;

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

    /**
     * Updates the config color value
     * @param r - Red Value
     * @param g - Green Value
     * @param b - Blue Value
     */
    private void updateConfig(int r, int g, int b) {
        switch(currentTab){
            case DEFAULT_TILE_TAB:
                Config.setDefaultColor(r, g, b);
                break;
            case FIRST_TILE_TAB:
                Config.setFirstColor(r, g, b);
                break;
            case SECOND_TILE_TAB:
                Config.setSecondColor(r, g, b);
                break;
        }
    }

    /**
     * Inner Class for the SeekBar updates
     * the preview color during SeekBar Change
     * and saves it afterwards
     */
    private class redSeekBarChange implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean userInput) {
            if(userInput)
                preview.setBackgroundColor(Color.argb(255, i, greenBarValue, blueBarValue));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            redBarValue = seekBar.getProgress();
            updateConfig(redBarValue, greenBarValue, blueBarValue);
        }
    }

    /**
     * Inner Class for the SeekBar updates
     * the preview color during SeekBar Change
     * and saves it afterwards
     */
    private class GreenSeekBarChange implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean userInput) {
            if(userInput)
                preview.setBackgroundColor(Color.argb(255, redBarValue, i, blueBarValue));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            greenBarValue = seekBar.getProgress();
            updateConfig(redBarValue, greenBarValue, blueBarValue);
        }
    }

    /**
     * Inner Class for the SeekBar updates
     * the preview color during SeekBarChange
     * and saves it afterwards
     */
    private class BlueSeekBarChange implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean userInput) {
            if(userInput)
                preview.setBackgroundColor(Color.argb(255, redBarValue, greenBarValue, i));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            blueBarValue = seekBar.getProgress();
            updateConfig(redBarValue, greenBarValue, blueBarValue);
        }
    }
}


