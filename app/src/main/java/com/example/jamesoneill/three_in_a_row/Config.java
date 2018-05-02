package com.example.jamesoneill.three_in_a_row;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by James O'Neill on 20/03/2018.
 */

public class Config
{
    private static int colNumbers;
    private static int timerSeconds = 30;
    private static int firstColor;
    private static int secondColor;
    private static int defaultColor;

    public static void loadSettings(Context context){
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        colNumbers = preferences.getInt("colNumbers", 4);
        firstColor = preferences.getInt("defaultColor", Color.GRAY);
        secondColor = preferences.getInt("firstColor", Color.BLACK);
        defaultColor = preferences.getInt("secondColor", Color.WHITE);
    }

    public static void saveSettings(Context context){
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("colNumbers", getColNumbers());
        editor.putInt("defaultColor", getDefaultColor());
        editor.putInt("firstColor", getFirstColor());
        editor.putInt("secondColor", getSecondColor());

        editor.commit();
    }

    public static void setColNumbers(int colNumbers){
        Config.colNumbers = colNumbers;
    }

    public static int getColNumbers() {
        return Config.colNumbers;
    }

    public static void setDefaultColor(int r, int g, int b){
        defaultColor = Color.argb(255, r, g, b);
    }

    public static int getDefaultColor() {
        return defaultColor;
    }

    public static void setFirstColor(int r, int g, int b){
        firstColor = Color.argb(255, r, g, b);
    }

    public static int getFirstColor() {
        return firstColor;
    }

    public static void setSecondColor(int r, int g, int b){
        secondColor = Color.argb(255, r, g, b);
    }

    public static int getSecondColor() {
        return secondColor;
    }

    public static void setTimerSeconds(int timerSeconds){
        Config.timerSeconds = timerSeconds;
    }

    public static int getTimerSeconds() {
        return Config.timerSeconds;
    }


}
