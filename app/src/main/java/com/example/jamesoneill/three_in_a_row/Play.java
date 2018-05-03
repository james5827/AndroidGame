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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Activity class that houses the play screen
 * This Screen encapsulates the entire game and game logic
 */
public class Play extends AppCompatActivity {

    //Colors applied on click
    //These Colors alternate based off of the boolean useFirst
    private int firstColor;
    private int secondColor;
    private boolean useFirst = false;

    /*
      Parallel array that translates to the one dimensional
      grid array in order to ease manipulation and the execution of game logic
     */
    private byte[][] colorTracker;

    //Color Constants
    private final byte FIRST_COLOR = 1;
    private final byte SECOND_COLOR = 2;

    //Custom Clock Constants
    private Handler clockHandler;
    private Handler timeOutHandler;
    private boolean activeClock = true;

    private byte turnTracker = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                Log.i("array", ""+ Arrays.deepToString(colorTracker))
        );

        //View that displays the next color
        View preview = findViewById(R.id.previewColor);

        ArrayList<View> gridTiles = new ArrayList<>();

        //Sets all view background color and adds it to the array
        int defaultColor = Config.getDefaultColor();
        byte numberOfTiles = (byte)Math.pow(Config.getColNumbers(), 2);
        for (int i = 0; i< numberOfTiles; ++i) {
            View tile = new View(this);
            tile.setBackgroundColor(defaultColor);
            gridTiles.add(tile);
        }

        colorTracker = new byte[Config.getColNumbers()][Config.getColNumbers()];

        //array that tracks the tiles to be pre-selected
        byte[] randomNumbers = new byte[4];

        //sets the first value to be preselected
        randomNumbers[0] = (byte) (Math.random() * numberOfTiles);
        //sets the rest of the values to be preselected
        //and checks if that value is already in the array
        for (byte i = 1; i < randomNumbers.length; ++i)
        {
            byte primary = (byte) (Math.random() * numberOfTiles);
            boolean valid = true;
            for(byte x = (byte)(i - 1); x >= 0; --x)
                if (primary == randomNumbers[x]) {
                    valid = false;
                    break;
                }

            if(valid)
                randomNumbers[i] = primary;
            else
                --i;
        }

        firstColor = Config.getFirstColor();
        secondColor = Config.getSecondColor();

        preview.setBackgroundColor(secondColor);

        //set half of the random numbers array to the first color
        byte[] randomNumbers1 = {randomNumbers[0], randomNumbers[1]};
        for(byte val : randomNumbers1) {
            byte x = (byte) (val/Config.getColNumbers());
            byte y = (byte) (val%Config.getColNumbers());
            colorTracker[x][y] = FIRST_COLOR;
            gridTiles.get(val).setBackgroundColor(firstColor);
        }

        //set half the random numbers array to the second color
        byte[] randomNumbers2 = {randomNumbers[2], randomNumbers[3]};
        for(byte val : randomNumbers2) {
            byte x = (byte) (val/Config.getColNumbers());
            byte y = (byte) (val%Config.getColNumbers());
            colorTracker[x][y] = SECOND_COLOR;
            gridTiles.get(val).setBackgroundColor(secondColor);
        }

        //get application display metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //construct grid
        GridAdapter adapter = new GridAdapter(gridTiles, this, metrics, randomNumbers);
        GridView gameBoard = findViewById(R.id.game_board);

        gameBoard.setNumColumns(Config.getColNumbers());
        gameBoard.setAdapter(adapter);

        gameBoard.setOnItemClickListener(this::tileClick);

        //create clock
        TextView clock = findViewById(R.id.clockView);
        clock.setText(formatClockString(Config.getTimerSeconds()));

        //handles the updating of the clock
        clockHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (activeClock) {
                    Bundle bundle = msg.getData();
                    int time = bundle.getInt("second");
                    clock.setText(formatClockString(time));
                }
            }
        };

        //handles the timeout
        timeOutHandler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();

                if(bundle.getBoolean("Time Out")) {
                    gameOverAlert();
                }
            }
        };

        startClock();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        activeClock = false;

        int id = item.getItemId();
        Intent i = new Intent();

        switch (id) {
            case R.id.play_menu_home:
                i = new Intent(this, Home.class);
                break;
            case R.id.play_menu_restart:
                i = getIntent();
                break;
            case R.id.play_menu_settings:
                i = new Intent(this, Settings.class);
                break;
            case R.id.play_menu_help:
                i = new Intent(this, Help.class);
                break;
        }

        startActivity(i);
        finish();
        return super.onOptionsItemSelected(item);
    }

    /**
     * Deactivates the timer
     */
    @Override
    public void onBackPressed() {
        activeClock = false;
        super.onBackPressed();
    }

    /**
     * Runs the timer on another thread and updates
     * the UI through the handlers
     */
    private void startClock()
    {
        Runnable clock = () -> {
            try{
                for(int i = Config.getTimerSeconds(); i >= 0; --i) {
                    if (activeClock) {
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putInt("second", i);
                        Thread.sleep(1000);
                        message.setData(bundle);
                        clockHandler.sendMessage(message);
                    }
                    else
                        break;
                }
            }catch (InterruptedException ignored){}

            if(activeClock) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putBoolean("Time Out", true);
                message.setData(bundle);
                timeOutHandler.sendMessage(message);
            }
        };

        Thread clockThread = new Thread(clock);
        clockThread.start();
    }

    /**
     * Formats the remaining time for UI
     * @param time the seconds left to be formatted
     * @return the formatted string
     */
    private String formatClockString(int time){
        int seconds = time%60;
        return "0" + time/60 + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    /**
     * Click Handler for the grid tiles
     * Changes the color of the clicked tile to the next color
     * and fires the game logic
     *
     * @param adapterView the grid adapter
     * @param view the grid tile
     * @param i tile position
     * @param l the tile id
     */
    private void tileClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        //increment the number of turns
        ++turnTracker;

        //convert to 2 dimensional array from tile position
        byte x = (byte) (i/Config.getColNumbers());
        byte y = (byte) (i%Config.getColNumbers());

        //update the preview color
        View preview = findViewById(R.id.previewColor);
        preview.setBackgroundColor(useFirst ? secondColor: firstColor);

        //change the tile background color
        view.setBackgroundColor(useFirst ? firstColor: secondColor);
        GridAdapter adapter = (GridAdapter) adapterView.getAdapter();

        //update color tracker
        colorTracker[x][y] = useFirst ? FIRST_COLOR : SECOND_COLOR;

        //disable tile from clicks
        adapter.enabled[i] = false;
        useFirst = !useFirst;

        //run gamelogic
        gameLogic(x, y);
    }

    /**
     * Takes a coordinate from the grid and checks the vertical and horizontal adjacent tiles
     * and counts how many are in a row.
     * @param x
     * @param y
     */
    private void gameLogic(byte x, byte y)
    {
        //Left of tile check
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
            }catch (ArrayIndexOutOfBoundsException Exception){
                break;
            }
        }

        if(inARow == 3) {
            gameOverAlert();
            return;
        }

        //Right of tile check
        for(int i = 1; i <= 2; ++i)
        {
            try{
                if(colorTracker[x][y] == colorTracker[x][y + i]) {
                    ++inARow;
                    if(inARow == 3) {
                        gameOverAlert();
                        return;
                    }
                }
                else {
                    break;
                }
            }catch (ArrayIndexOutOfBoundsException Exception){
                break;
            }
        }

        inARow = 1;

        //Up of tile Check
        for (int i = 1; i <= 2; ++i) {
            try {
                if (colorTracker[x][y] == colorTracker[x - i][y]) {
                    ++inARow;
                } else {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException Exception) {
                break;
            }
        }

        if(inARow == 3) {
            gameOverAlert();
            return;
        }

        //Down of tile check
        for (int i = 1; i <= 2; ++i) {
            try {
                if (colorTracker[x][y] == colorTracker[x + i][y]) {
                    ++inARow;
                    if(inARow == 3) {
                        gameOverAlert();
                    }
                } else {
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException Exception) {
                break;
            }
        }

        if(turnTracker == Math.pow(Config.getColNumbers(), 2))
            gameWinAlert();
    }

    /**
     * Displays game over alert box and presents a quit or restart option
     */
    private void gameOverAlert()
    {
        activeClock = false;
        GridView gameBoard = findViewById(R.id.game_board);
        gameBoard.setEnabled(false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.lose)
                .setTitle(R.string.lose_title)
                .setPositiveButton("Play Again", (dialog, id) -> {
                    startActivity(getIntent());
                    finish();
                })
                .setNegativeButton("Back", (dialog, id) -> finish())
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Displays a game win alert box and presents a quit or restart option
     */
    private void gameWinAlert(){
        activeClock = false;
        TextView clock = findViewById(R.id.clockView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(clock.getText())
                .setTitle("You Win!")
                .setPositiveButton("Play Again", (dialog, id) -> {
                    startActivity(getIntent());
                    finish();
                })
                .setNegativeButton("Back", (dialog, id) -> finish())
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
