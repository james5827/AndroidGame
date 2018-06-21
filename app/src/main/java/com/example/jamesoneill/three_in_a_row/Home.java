package com.example.jamesoneill.three_in_a_row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

/**
 * Activity class for the home screen that
 * this screen acts as the navigation hub
 * and displays the current high scores
 */
public class Home extends AppCompatActivity {

    private static final String TAG = "home";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Config.loadSettings(this.getApplicationContext());

        if(getIntent().hasExtra(Config.TUTORIAL)){
            Log.i(TAG, "onCreate: It worked maybe");
            tutorialShowCaseView();
        }
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

    private void tutorialShowCaseView(){
        new scvTutorial(this);
    }

    private static class scvTutorial implements View.OnClickListener{
        private byte counter;
        private ShowcaseView scv;
        private Activity context;

        private scvTutorial(Activity context) {
            this.counter = 0;
            this.context = context;
            this.scv = new ShowcaseView.Builder(context)
                .withNewStyleShowcase()
                .setStyle(R.style.CustomShowcaseTheme)
                .blockAllTouches()
                .setTarget(Target.NONE)
                .setContentTitle("This is the Home Screen")
                .setContentText("From here you can navigate around the rest of the application")
                .setOnClickListener(this)
                .build();

            scv.setButtonText("Next");
        }

        @Override
        public void onClick(View view) {
            switch (counter) {
                case 0:
                    scv.setShowcase(new ViewTarget(R.id.btn_settings, context), false);
                break;
                case 1:
                    scv.setShowcase(new ViewTarget(R.id.btn_highscores, context), true);
                break;
                case 2:
                    scv.setShowcase(new ViewTarget(R.id.btn_help, context), true);
                break;
                case 3:
                    scv.setShowcase(new ViewTarget(R.id.btn_play, context), true);
                break;
            }
            ++counter;
        }
    }
}
