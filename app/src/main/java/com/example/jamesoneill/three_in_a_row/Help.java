package com.example.jamesoneill.three_in_a_row;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Activity class for the help screen
 * This activity presents the user with
 * Instructions on how to play the game
 */
public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button tutorial = findViewById(R.id.button);
        tutorial.setOnClickListener((view) -> {
            new ShowcaseView.Builder(this)
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .blockAllTouches()
                    .setTarget(new ViewTarget(R.id.button, this))
                    .setContentTitle("Showcase View Library")
                    .setContentText("This Library Will Be Used For An In App Tutorial")
                    .setOnClickListener((View view1) -> Config.createShowCaseIntent(Help.this, Home.class))
                    .build();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent i = new Intent();

        switch (id) {
            case R.id.help_menu_home:
                i = new Intent(this, Home.class);
                break;
            case R.id.help_menu_play:
                i = new Intent(this, Play.class);
                break;
            case R.id.help_menu_settings:
                i = new Intent(this, Settings.class);
                break;
        }

        startActivity(i);
        return super.onOptionsItemSelected(item);
    }
}
