package com.example.jamesoneill.three_in_a_row;

import android.content.Context;
import android.graphics.Color;

/**
 * Created by James O'Neill on 20/03/2018.
 */

public class Config
{
    private static int colNumbers = 4;
    private static int timerSeconds = 30;
    private static int firstColor = Color.BLACK;
    private static int secondColor = Color.WHITE;
    private static int defaultColor = Color.GRAY;

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

    public static int getTimerSeconds() {
        return timerSeconds;
    }
}
