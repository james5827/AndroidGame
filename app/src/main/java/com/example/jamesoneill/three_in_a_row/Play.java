package com.example.jamesoneill.three_in_a_row;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Play extends AppCompatActivity {

    private int firstColor;
    private int secondColor;
    private boolean useFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
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

        ArrayList<View> gridTiles = new ArrayList<>();

        int color = Config.getFirstColor(this);
        for (int i = 0; i< Math.pow(Config.getColNumbers(), 2); ++i) {
            View tile = new View(this);
            gridTiles.add(tile);
        }

        gridTiles.get(4).setBackgroundColor(color);
        gridTiles.get(8).setBackgroundColor(color);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridAdapter adapter = new GridAdapter(gridTiles, this, metrics);
        GridView gameBoard = findViewById(R.id.game_board);

        gameBoard.setNumColumns(Config.getColNumbers());
        gameBoard.setAdapter(adapter);

        gameBoard.setOnItemClickListener(this::lambdaTest);

        firstColor = Config.getFirstColor(this);
        secondColor = Config.getSecondColor(this);
    }

    private void lambdaTest(AdapterView<?> adapterView, View view, int i, long l)
    {
        view.setBackgroundColor(useFirst ? firstColor: secondColor);

        GridAdapter adapter = (GridAdapter) adapterView.getAdapter();

        adapter.enabled[i] = false;
        useFirst = !useFirst;
    }

}
