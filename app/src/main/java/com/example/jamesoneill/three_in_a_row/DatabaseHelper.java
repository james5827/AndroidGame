package com.example.jamesoneill.three_in_a_row;

public interface DatabaseHelper {
    String TAG = "Database";

    String DATABASE_NAME = "highscores";
    int DATABASE_VERSION = 1;

    String TABLE_Scores = "scores";

    String KEY_SCORE_ID = "score_id";
    String KEY_SCORE_NAME = "name";
    String KEY_SCORE_GRIDSIZE = "grid_size";
    String KEY_SCORE_DIFFICULTY = "difficulty";
    String KEY_SCORE_TIME = "time";

    String CREATE_TABLE_SCORES =
            "CREATE TABLE " + TABLE_Scores + "(" +
            KEY_SCORE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_SCORE_NAME + " TEXT, " +
            KEY_SCORE_GRIDSIZE + " TEXT, " +
            KEY_SCORE_DIFFICULTY + " TEXT, " +
            KEY_SCORE_TIME + " TEXT" +
            ")";

    String TEST_Easy = "INSERT INTO " + TABLE_Scores + " VALUES (NULL, 'James Jon', 4, 'Easy', '00:23');";
    String TEST_Medium = "INSERT INTO " + TABLE_Scores + " VALUES (NULL, 'James Jon', 5, 'Medium', '00:23');";
    String TEST_Hard = "INSERT INTO " + TABLE_Scores + " VALUES (NULL, 'James Jon', 6, 'Hard', '00:23');";

    String DROP_TABLE_SCORES = "DROP TABLE IF EXISTS " + TABLE_Scores;
}
