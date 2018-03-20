package com.example.jamesoneill.three_in_a_row;

import android.content.Context;

/**
 * Created by James O'Neill on 20/03/2018.
 */

public class Config
{
    private static int colNumbers = 5;

    public static void setColNumbers(int colNumbers){
        colNumbers = colNumbers;
    }

    public static int getColNumbers() {
        return colNumbers;
    }

    public static int getFirstColor(Context context)
    {
        return context.getResources().getColor(R.color.firstColor);
    }

    public static int getSecondColor(Context context)
    {
        return context.getResources().getColor(R.color.secondColor);
    }


}
