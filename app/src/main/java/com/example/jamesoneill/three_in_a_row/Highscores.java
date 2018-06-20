package com.example.jamesoneill.three_in_a_row;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Highscores extends AppCompatActivity {

    SeekBar gridSizeSeek;
    Spinner difficultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.difficultySpinner = findViewById(R.id.highScoreSpinner);
        initialiseGridSizeSeekBar();
    }

    public void displayScores(View view){
        HighscoreDatabase db = new HighscoreDatabase(this);

        List<Score> scores = db.getHighScores(difficultySpinner.getSelectedItem().toString(), (byte) (gridSizeSeek.getProgress() + 4));

        RecyclerView recyclerView = findViewById(R.id.highScoreRecyclerView);
        recyclerView.setAdapter(new ScoreRVAdapter(this, scores));
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
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        this.gridSizeSeek.setProgress(Config.getColNumbers() -4);
        gridViewlbl.setText(String.format(getString(R.string.grid_size_seek_bar), Config.getColNumbers()));
    }
}
