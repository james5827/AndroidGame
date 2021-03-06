package com.example.jamesoneill.three_in_a_row;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by James O'Neill on 20/03/2018.
 * Global Settings Class that provides utility functions for
 * for getting and manipulating the game settings as well as saving
 * them to local storage.
 */
public class Config
{
    //settings
    private static int colNumbers;
    private static int timerSeconds;
    private static int firstColor;
    private static int secondColor;
    private static int defaultColor;
    public static final String TUTORIAL = "Tutorial";

    /**
     * Load settings from sharedPreference
     * @param context
     */
    public static void loadSettings(Context context){
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);

        Config.colNumbers = preferences.getInt("colNumbers", 4);
        Config.defaultColor = preferences.getInt("defaultColor", Color.GRAY);
        Config.firstColor = preferences.getInt("firstColor", Color.BLACK);
        Config.secondColor = preferences.getInt("secondColor", Color.WHITE);
        Config.timerSeconds = preferences.getInt("timerSeconds", 30);
    }

    /**
     * Save settings to sharedPreference
     * @param context - application context in which to access Shared Preferences
     */
    public static void saveSettings(Context context){
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("colNumbers", getColNumbers());
        editor.putInt("defaultColor", getDefaultColor());
        editor.putInt("firstColor", getFirstColor());
        editor.putInt("secondColor", getSecondColor());
        editor.putInt("timerSeconds", getTimerSeconds());
        editor.commit();
    }

    /**
     * Set column numbers
     * @param colNumbers
     */
    public static void setColNumbers(int colNumbers){
        Config.colNumbers = colNumbers;
    }

    /**
     * Get column numbers
     * @return Number of columns
     */
    public static int getColNumbers() {
        return Config.colNumbers;
    }

    /**
     * Set the default tile color via RGB
     * @param r Red Value
     * @param g Green Value
     * @param b Blue Value
     */
    public static void setDefaultColor(int r, int g, int b){
        defaultColor = Color.argb(255, r, g, b);
    }

    /**
     * Get the default tile color
     * @return Default tile color as a integer
     */
    public static int getDefaultColor() {
        return defaultColor;
    }

    /**
     * Set the first tile color via RGB
     * @param r Red Value
     * @param g Green Value
     * @param b Blue Value
     */
    public static void setFirstColor(int r, int g, int b){
        firstColor = Color.argb(255, r, g, b);
    }

    /**
     * Get the first tile color
     * @return first tile color as a integer
     */
    public static int getFirstColor() {
        return firstColor;
    }

    /**
     * Set the second tile color via RGB
     * @param r Red Value
     * @param g Green Value
     * @param b Blue Value
     */
    public static void setSecondColor(int r, int g, int b){
        secondColor = Color.argb(255, r, g, b);
    }

    /**
     * Get the second tile color
     * @return second tile color as a integer
     */
    public static int getSecondColor() {
        return secondColor;
    }

    /**
     * Set the timers seconds
     * @param timerSeconds
     */
    public static void setTimerSeconds(int timerSeconds){
        Config.timerSeconds = timerSeconds;
    }

    /**
     * Get the Timers Seconds
     * @return
     */
    public static int getTimerSeconds() {
        return Config.timerSeconds;
    }

    public static void createShowCaseIntent(Context context, Class<?> cls){
        Intent intent = new Intent(context, cls);
        intent.putExtra("Tutorial", true);
        context.startActivity(intent);
    }

    public static android.widget.RelativeLayout.LayoutParams getBottomLeftParams(Activity context) {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int margin = ((Number) (context.getResources().getDisplayMetrics().density * 16)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        return lps;
    }

    public static android.widget.RelativeLayout.LayoutParams getBottomRightParams(Activity context) {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        int margin = ((Number) (context.getResources().getDisplayMetrics().density * 16)).intValue();
        lps.setMargins(margin, margin, margin, margin);
        return lps;
    }
}
