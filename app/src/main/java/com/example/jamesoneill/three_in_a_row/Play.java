package com.example.jamesoneill.three_in_a_row;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity class that houses the play screen
 * This Screen encapsulates the entire game and game logic
 */
public class Play extends AppCompatActivity {

    //Colors applied on click
    //These Colors alternate based off of the boolean useFirst
    private int firstColor;
    private int secondColor;
    private boolean useFirst;
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
    private int secondsLeft;

    private byte turnTracker = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int defaultColor = Config.getDefaultColor();
        firstColor = Config.getFirstColor();
        secondColor = Config.getSecondColor();
        byte numberOfTiles = (byte)Math.pow(Config.getColNumbers(), 2);
        View preview = findViewById(R.id.previewColor);

        byte[] initiatedColors;
        byte[] firstColorArray;
        byte[] secondColorArray;

        ArrayList<View> gridTiles = new ArrayList<>();
        //Sets all view background color and adds it to the array
        for (int i = 0; i< numberOfTiles; ++i) {
            View tile = new View(this);
            tile.setBackgroundColor(defaultColor);
            gridTiles.add(tile);
        }

        if (savedInstanceState != null) {
            byte[] initGridColor = new byte[Config.getColNumbers() * Config.getColNumbers()];
            byte position = 0;
            colorTracker = (byte[][]) savedInstanceState.getSerializable("colorTracker");

            byte firstColorCount = 0;
            byte secondColorCount = 0;
            for(byte i = 0; i < colorTracker.length; ++i){
                for (byte x = 0; x < colorTracker[i].length; ++x, ++position) {
                    initGridColor[position] = colorTracker[i][x];
                    if(initGridColor[position] == 1){
                        ++firstColorCount;
                    }
                    if (initGridColor[position] == 2){
                        ++secondColorCount;
                    }
                }
            }

            initiatedColors = new byte[firstColorCount + secondColorCount];

            position = 0;
            for(byte i = 0, x = 0; i< initGridColor.length; ++i, ++position){
                if (initGridColor[i] == 1 || initGridColor[i] == 2) {
                    initiatedColors[x] = position;
                    ++x;

                    if(initGridColor[i] == 2){
                        gridTiles.get(i).setBackgroundColor(secondColor);
                    } else {
                        gridTiles.get(i).setBackgroundColor(firstColor);
                    }
                }
            }
            useFirst = savedInstanceState.getBoolean("useFirst");
            turnTracker = savedInstanceState.getByte("turnTracker");
            secondsLeft = savedInstanceState.getInt("secondsLeft");

            Log.i("State", "onCreate: Receving " + useFirst);
            preview.setBackgroundColor(useFirst ? firstColor: secondColor);
        } else {
            colorTracker = new byte[Config.getColNumbers()][Config.getColNumbers()];
            //array that tracks the tiles to be pre-selected
            initiatedColors = new byte[4];

            //sets the first value to be preselected
            initiatedColors[0] = (byte) (Math.random() * numberOfTiles);

            //sets the rest of the values to be preselected
            //and checks if that value is already in the array
            for (byte i = 1; i < initiatedColors.length; ++i) {
                byte primary = (byte) (Math.random() * numberOfTiles);
                boolean valid = true;
                for(byte x = (byte)(i - 1); x >= 0; --x)
                    if (primary == initiatedColors[x]) {
                        valid = false;
                        break;
                    }

                if(valid)
                    initiatedColors[i] = primary;
                else
                    --i;
            }

            //set half of the random numbers array to the first color
            firstColorArray = new byte[]{initiatedColors[0], initiatedColors[1]};
            for(byte val : firstColorArray) {
                byte x = (byte) (val/Config.getColNumbers());
                byte y = (byte) (val%Config.getColNumbers());
                colorTracker[x][y] = FIRST_COLOR;
                gridTiles.get(val).setBackgroundColor(firstColor);
            }

            secondColorArray = new byte[]{initiatedColors[2], initiatedColors[3]};
            for(byte val : secondColorArray) {
                byte x = (byte) (val/Config.getColNumbers());
                byte y = (byte) (val%Config.getColNumbers());
                colorTracker[x][y] = SECOND_COLOR;
                gridTiles.get(val).setBackgroundColor(secondColor);
            }

            useFirst = false;
            secondsLeft = Config.getTimerSeconds();
            preview.setBackgroundColor(secondColor);
        }

        //get application display metrics
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        //construct grid
        GridAdapter adapter = new GridAdapter(gridTiles, this, metrics, initiatedColors);
        GridView gameBoard = findViewById(R.id.game_board);

        gameBoard.setNumColumns(Config.getColNumbers());
        gameBoard.setAdapter(adapter);

        gameBoard.setOnItemClickListener(this::tileClick);

        //create clock
        TextView clock = findViewById(R.id.clockView);
        clock.setText(formatClockString(secondsLeft));

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

        if(getIntent().hasExtra(Config.TUTORIAL)){
            tutorialShowCaseView();
        }
        else{
            startClock();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        this.activeClock = false;

        outState.putSerializable("colorTracker", colorTracker);
        outState.putByte("turnTracker", turnTracker);
        outState.putInt("secondsLeft", secondsLeft);
        outState.putBoolean("useFirst", useFirst);
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
                for(int i = Play.this.secondsLeft; i >= 0; --i) {
                    if (activeClock) {
                        --Play.this.secondsLeft;
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

        //run game logic
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
        for(int i = 1; i <= 2; ++i) {
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
        for(int i = 1; i <= 2; ++i) {
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

        dialog.getWindow().setGravity(Gravity.BOTTOM);

        dialog.show();
    }

    /**
     * Displays a game win alert box and presents a quit or restart option
     */
    private void gameWinAlert(){
        activeClock = false;
        TextView clock = findViewById(R.id.clockView);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        if(compareTimes(clock.getText().toString())){

            String difficulty = "";

            switch (Config.getTimerSeconds()){
                case 45:
                    difficulty = "Easy";
                    break;
                case 30:
                    difficulty = "Medium";
                    break;
                case 15:
                    difficulty = "Hard";
                    break;
            }

            View view = getLayoutInflater().inflate(R.layout.highscore_dialog, null);
            Button quitBtn = view.findViewById(R.id.quitBtn);
            Button submitBtn = view.findViewById(R.id.submitBtn);
            EditText editText = view.findViewById(R.id.nameEditText);

            String finalDifficulty = difficulty;
            submitBtn.setOnClickListener((View view2) -> {
                Score score = new Score(1,
                        editText.getText().toString(),
                        (byte)Config.getColNumbers(),
                        finalDifficulty,
                        clock.getText().toString());

                HighscoreDatabase db = new HighscoreDatabase(this);
                db.insertScore(score);
                finish();
            });

            quitBtn.setOnClickListener((View view1) -> finish());

            builder.setView(view);
        } else {
            builder.setMessage(clock.getText())
                .setTitle("You Win!")
                .setPositiveButton("Play Again", (dialog, id) -> {
                    startActivity(getIntent());
                    finish();
                })
                .setNegativeButton("Back", (dialog, id) -> finish())
                .setCancelable(false);
        }

        AlertDialog dialog = builder.create();
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }

    private boolean compareTimes(String time){
        HighscoreDatabase db = new HighscoreDatabase(this);

        String difficulty = "";

        switch (Config.getTimerSeconds()){
            case 45:
                difficulty = "Easy";
                break;
            case 30:
                difficulty = "Medium";
                break;
            case 15:
                difficulty = "Hard";
                break;
        }

        List<Score> scores = db.getHighScores(difficulty, (byte)Config.getColNumbers());

        if(scores.size() <= 10) {
            return true;
        } else {
            int size = scores.size();
            for (byte x = 0; x < size; ++x){
                if(time.compareTo(scores.get(x).getTime()) < 0) {
                    db.deleteScore(scores.get(--size).getId());
                    return true;
                }
            }
        }

        return false;
    }

    public void tutorialShowCaseView(){
        new scvPlayTutorial(this);
    }

    private static class scvPlayTutorial implements View.OnClickListener{
        private byte counter;
        private ShowcaseView scv;
        private Activity context;

        private scvPlayTutorial(Activity context) {
            this.counter = 0;
            this.context = context;
            this.scv = new ShowcaseView.Builder(context)
                    .withNewStyleShowcase()
                    .setStyle(R.style.CustomShowcaseTheme)
                    .setTarget(Target.NONE)
                    .setContentTitle("This is the game screen")
                    .setContentText("")
                    .setOnClickListener(this)
                    .build();

            scv.setButtonText("Next");
        }

        @Override
        public void onClick(View view) {
            switch (counter) {
                case 0:
                    scv.setShowcase(new ViewTarget(R.id.game_board, context), true);
                    scv.setContentTitle("This is the Grid");
                    scv.setContentText("Tap the tiles to fill the grid.");
                    break;
                case 1:
                    scv.setContentText("Don't get three colors in a row or else you lose.");
                    break;
                case 2:
                    scv.setShowcase(new ViewTarget(R.id.previewColor, context), true);
                    scv.setContentTitle("Color preview");
                    scv.setContentText("This shows the color shown on the next tap.");

                    if(context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                        scv.setButtonPosition(Config.getBottomLeftParams(context));
                    break;
                case 3:
                    scv.setShowcase(new ViewTarget(R.id.clockView, context), true);
                    scv.setContentTitle("Timer");
                    scv.setContentText("Fill the grid before the timer reaches zero");
                    scv.setButtonPosition(Config.getBottomRightParams(context));
                    break;
                case 4:
                    scv.setTarget(Target.NONE);
                    scv.setContentTitle("Winning");
                    scv.setContentText("If your time is fast enough you can submit you name for a High Score");
                    break;
                case 5:
                    scv.hide();
                    Config.createShowCaseIntent(context, Settings.class);
                    break;
            }
            ++counter;
        }
    }
}
