package com.example.jamesoneill.three_in_a_row;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.List;

public class Highscores extends AppCompatActivity {

    private SeekBar gridSizeSeek;
    private Spinner difficultySpinner;
    private RecyclerView recyclerView;
    private TextView noRecordstxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.highScoreRecyclerView);

        this.difficultySpinner = findViewById(R.id.highScoreSpinner);
        difficultySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                displayScores();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        initialiseGridSizeSeekBar();

        if(getIntent().hasExtra(Config.TUTORIAL)){
            tutorialShowCaseView();
        }

        noRecordstxt = findViewById(R.id.noRecordstxt);
    }

    public void displayScores(){
        recyclerView.setAdapter(null);
        noRecordstxt.setText("");
        HighscoreDatabase db = new HighscoreDatabase(this);

        List<Score> scores = db.getHighScores(difficultySpinner.getSelectedItem().toString(), (byte) (gridSizeSeek.getProgress() + 4));

        if(scores.size() > 0)
            recyclerView.setAdapter(new ScoreRVAdapter(this, scores));
        else
            noRecordstxt.setText(R.string.noRowstxt);
    }

    private void initialiseGridSizeSeekBar(){
        this.gridSizeSeek = findViewById(R.id.gridSeekHighScores);
        TextView gridViewlbl = findViewById(R.id.lblGridSeekHighScores);
        this.gridSizeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
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
                gridViewlbl.setText(String.format(getString(R.string.grid_size_seek_bar), i));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                displayScores();
            }
        });

        this.gridSizeSeek.setProgress(Config.getColNumbers() -4);
        gridViewlbl.setText(String.format(getString(R.string.grid_size_seek_bar), Config.getColNumbers()));
    }

    private void tutorialShowCaseView(){
        new Highscores.scvHighScoresTutorial(this);
    }

    private class scvHighScoresTutorial implements View.OnClickListener{
        private byte counter;
        private ShowcaseView scv;
        private Activity context;

        private scvHighScoresTutorial(Activity context) {
            this.counter = 0;
            this.context = context;
            this.scv = new ShowcaseView.Builder(context)
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setTarget(Target.NONE)
                    .setContentTitle("This is the High Score Screen")
                    .setContentText("Alter the grid size slider and the difficulty to see the best scores for games with those parameters")
                    .setOnClickListener(this)
                    .build();

            scv.setButtonText("Next");
        }

        @Override
        public void onClick(View view) {
            switch (counter) {
                case 0:
                    scv.setShowcase(new ViewTarget(R.id.gridSeekHighScores, context), true);
                    scv.setContentTitle("Grid Slider");
                    scv.setContentText("Change the grid slider");
                break;
                case 1:
                    scv.setShowcase(new ViewTarget(R.id.highScoreSpinner, context), true);
                    scv.setContentTitle("Difficulty Select Box");
                    scv.setContentText("Change the difficulty");
                    break;
                case 2:
                    scv.setShowcase(new ViewTarget(R.id.noRecordstxt, context), true);
                    scv.setContentTitle("Highscores");
                    scv.setContentText("Here are the high scores for those settings");
                    break;
                case 3:
                    scv.hide();
                    Intent intent = new Intent(context, Home.class);
                    context.startActivity(intent);
                    context.finish();
                    break;
            }
            ++counter;
        }
    }
}
