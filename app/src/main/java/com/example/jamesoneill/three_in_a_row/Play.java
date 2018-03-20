package com.example.jamesoneill.three_in_a_row;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class Play extends AppCompatActivity {

    private int firstColor;
    private int secondColor;
    private boolean useFirst = false;
    private byte[][] colorTracker;
    private final byte FIRST_COLOR = 1;
    private final byte SECOND_COLOR = 2;
    private Handler clockHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Log.i("array", ""+ Arrays.deepToString(colorTracker))
        );

        ArrayList<View> gridTiles = new ArrayList<>();

        int color = Config.getFirstColor(this);
        byte numberOfTiles = (byte)Math.pow(Config.getColNumbers(), 2);
        for (int i = 0; i< numberOfTiles; ++i) {
            View tile = new View(this);
            gridTiles.add(tile);
        }

        colorTracker = new byte[Config.getColNumbers()][Config.getColNumbers()];

        byte[] randomNumbers = new byte[2];

        //TODO: Make Check For Three in a row on creation
        randomNumbers[0] = (byte) (Math.random() * numberOfTiles);
        for (byte i = 1; i < randomNumbers.length; ++i)
        {
            byte primary = (byte) (Math.random() * numberOfTiles);
            boolean valid = true;
            for(byte x = (byte)(i - 1); x >= 0; --x)
                if (primary == randomNumbers[x])
                    valid = false;

            if(valid)
                randomNumbers[i] = primary;
            else
                --i;
        }

        for(byte val : randomNumbers)
        {
           byte x = (byte) (val/Config.getColNumbers());
           byte y = (byte) (val%Config.getColNumbers());


            colorTracker[x][y] = FIRST_COLOR;
            gridTiles.get(val).setBackgroundColor(color);
        }

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        GridAdapter adapter = new GridAdapter(gridTiles, this, metrics, randomNumbers);
        GridView gameBoard = findViewById(R.id.game_board);

        gameBoard.setNumColumns(Config.getColNumbers());
        gameBoard.setAdapter(adapter);

        gameBoard.setOnItemClickListener(this::tileClick);

        firstColor = Config.getFirstColor(this);
        secondColor = Config.getSecondColor(this);

        clockHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String time = bundle.getString("second");

                TextView view = findViewById(R.id.clockView);
                view.setText(time);
            }
        };

        runCode();
    }

    public void runCode()
    {
        Runnable clock = new Runnable() {
            @Override
            public void run() {
                try{
                    for(int i = 30; i >= 0; --i) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        Thread.sleep(1000);
                        bundle.putString("second", "" + i);
                        message.setData(bundle);
                        clockHandler.sendMessage(message);
                    }
                }catch (InterruptedException exception){

                }
            }
        };
        Thread thread = new Thread(clock);
        thread.start();
    }

    private void tileClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        byte x = (byte) (i/Config.getColNumbers());
        byte y = (byte) (i%Config.getColNumbers());

        View preview = findViewById(R.id.previewColor);

        preview.setBackgroundColor(useFirst ? secondColor: firstColor);

        view.setBackgroundColor(useFirst ? firstColor: secondColor);

        GridAdapter adapter = (GridAdapter) adapterView.getAdapter();

        colorTracker[x][y] = useFirst ? FIRST_COLOR : SECOND_COLOR;

        adapter.enabled[i] = false;
        useFirst = !useFirst;

        gameLogic(x, y);
    }

    private void gameLogic(byte x, byte y)
    {
        byte inARow = 1;
        for(int i = 1; i <= 2; ++i)
        {
            try{
                if(colorTracker[x][y] == colorTracker[x][y - i]) {
                    ++inARow;
                }
                else {
                    break;
                }

                if(inARow == 3) {
                    gameOverAlert();
                    return;
                }

            }catch (ArrayIndexOutOfBoundsException Exception){
                break;
            }
        }

        for(int i = 1; i <= 2; ++i)
        {
            try{
                if(colorTracker[x][y] == colorTracker[x][y + i]) {
                    ++inARow;
                }
                else {
                    break;
                }

                if(inARow == 3) {
                    gameOverAlert();
                    return;
                }

            }catch (ArrayIndexOutOfBoundsException Exception){
                break;
            }
        }

            inARow = 1;

            for (int i = 1; i <= 2; ++i) {
                try {
                    if (colorTracker[x][y] == colorTracker[x - i][y]) {
                        ++inARow;
                    } else {
                        break;
                    }

                    if(inARow == 3) {
                        gameOverAlert();
                        return;
                    }

                } catch (ArrayIndexOutOfBoundsException Exception) {
                    break;
                }
            }

            for (int i = 1; i <= 2; ++i) {
                try {
                    if (colorTracker[x][y] == colorTracker[x + i][y]) {
                        ++inARow;
                    } else {
                        break;
                    }

                    if(inARow == 3) {
                        gameOverAlert();
                        return;
                    }

                } catch (ArrayIndexOutOfBoundsException Exception) {
                    break;
                }
            }
        }

    private void gameOverAlert()
    {
        GridView gameBoard = findViewById(R.id.game_board);
        gameBoard.setEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lose)
                .setTitle(R.string.lose_title)
                .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        startActivity(getIntent());
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), Home.class);
                        startActivity(i);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
