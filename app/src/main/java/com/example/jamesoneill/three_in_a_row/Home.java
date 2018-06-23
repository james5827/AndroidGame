package com.example.jamesoneill.three_in_a_row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
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
public class Home extends AppCompatActivity implements GestureDetector.OnGestureListener{

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Config.loadSettings(this.getApplicationContext());

        mDetector = new GestureDetectorCompat(this, this);

        if(getIntent().hasExtra(Config.TUTORIAL)){
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
                i = new Intent(this, Home.class);
                i.putExtra("Tutorial", true);
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
        tutorialShowCaseView();
    }

    public void openHighScoresActivity(View view) {
        startActivity(new Intent(this, Highscores.class));
    }

    private void tutorialShowCaseView(){
        new scvHomeTutorial(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) { return false; }

    @Override
    public void onShowPress(MotionEvent motionEvent) { }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) { return false; }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) { return false; }

    @Override
    public void onLongPress(MotionEvent motionEvent) { }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        openPlayActivity(new View(this));
        return true;
    }

    private class scvHomeTutorial implements View.OnClickListener{
        private byte counter;
        private ShowcaseView scv;
        private Activity context;

        private scvHomeTutorial(Activity context) {
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
                    scv.setContentTitle("Settings Button");
                    scv.setContentText("This button will take you to the settings screen, where you can change game settings.");
                break;
                case 1:
                    scv.setShowcase(new ViewTarget(R.id.btn_highscores, context), true);
                    scv.setContentTitle("High Scores Button");
                    scv.setContentText("This button will take you to the high score screen, where you can see your highscores.");
                break;
                case 2:
                    scv.setShowcase(new ViewTarget(R.id.btn_help, context), true);
                    scv.setContentTitle("Tutorial Button");
                    scv.setContentText("This button will give you a tour of the application");
                    scv.setButtonPosition(Config.getBottomLeftParams(context));
                break;
                case 3:
                    scv.setShowcase(new ViewTarget(R.id.btnTextView, context), true);
                    scv.setContentTitle("Play");
                    scv.setContentText("Swiping will take you to the game screen.");
                break;
                case 4:
                    scv.hide();
                    Config.createShowCaseIntent(Home.this, Play.class);
                break;
            }
            ++counter;
        }
    }
}
