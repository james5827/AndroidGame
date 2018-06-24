package com.example.jamesoneill.three_in_a_row;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class HighscoreDatabase extends SQLiteOpenHelper implements DatabaseHelper{
    public HighscoreDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SCORES);

        sqLiteDatabase.execSQL(TEST_Easy);
        sqLiteDatabase.execSQL(TEST_Medium);
        sqLiteDatabase.execSQL(TEST_Hard);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1){
        sqLiteDatabase.execSQL(DROP_TABLE_SCORES);

        onCreate(sqLiteDatabase);
    }

    public void insertScore(Score score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE_NAME, score.getName());
        values.put(KEY_SCORE_GRIDSIZE, score.getGridSize());
        values.put(KEY_SCORE_DIFFICULTY, score.getDifficulty());
        values.put(KEY_SCORE_TIME, score.getTime());

        db.insertOrThrow(TABLE_Scores, null ,values);
        db.close();
    }

    public void deleteScore(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_Scores, "id = " + id, null);
    }

    public ArrayList<Score> getHighScores(String difficulty, byte gridSize){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Score> scores = new ArrayList<>();

        String sql =
                "SELECT * FROM " + TABLE_Scores + " " +
                        "WHERE " + KEY_SCORE_GRIDSIZE + " = " + gridSize + " " +
                        "AND " + KEY_SCORE_DIFFICULTY + " = ? " +
                        "ORDER BY " + KEY_SCORE_TIME + " DESC  " +
                        "LIMIT 10";

       Cursor c = db.rawQuery(sql, new String[] {difficulty});

        if(c.moveToFirst())
            do {
                scores.add(new Score(
                        c.getInt(c.getColumnIndex(KEY_SCORE_ID)),
                        c.getString(c.getColumnIndex(KEY_SCORE_NAME)),
                        (byte) c.getInt(c.getColumnIndex(KEY_SCORE_GRIDSIZE)),
                        c.getString(c.getColumnIndex(KEY_SCORE_DIFFICULTY)),
                        c.getString(c.getColumnIndex(KEY_SCORE_TIME))
                ));
            }while(c.moveToNext());

        c.close();
        db.close();

        return scores;
    }
}
