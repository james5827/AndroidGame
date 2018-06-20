package com.example.jamesoneill.three_in_a_row;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Activity class for the home screen that
 * this screen acts as the navigation hub
 * and displays the current high scores
 */
public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Config.loadSettings(this.getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = new Intent();
        switch(id){
            case R.id.home_menu_play:
                i = new Intent(this, Play.class);
                break;
            case R.id.home_menu_settings:
                i = new Intent(this, Settings.class);
                break;
            case R.id.home_menu_help:
                i = new Intent(this, Help.class);
                break;
        }

        startActivity(i);
        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens the play activity
     * @param view the button that was clicked
     */
    public void openPlayActivity(View view) {
        Intent play = new Intent(this, Play.class);
        startActivity(play);
    }

    /**
     * Opens the settings activity
     * @param view the button that was clicked
     */
    public void openSettingsActivity(View view) {
        Intent settings = new Intent(this, Settings.class);
        startActivity(settings);
    }

    /**
     * Opens the help activity
     * @param view the button that was clicked
     */
    public void openHelpActivity(View view) {
        Intent help = new Intent(this, Help.class);
        startActivity(help);
    }

    public void openHighScoresActivity(View view) {
        startActivity(new Intent(this, Highscores.class));
    }
}
