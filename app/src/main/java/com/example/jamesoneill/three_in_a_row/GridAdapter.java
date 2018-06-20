package com.example.jamesoneill.three_in_a_row;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

/**
 * Custom Adapter for a grid view that inflates a view and sets its color
 * within the grid view
 */
public class GridAdapter extends BaseAdapter {

    //Arraylist of tiles
    private ArrayList<View> list;
    //Array of items that can trigger clicks
    public boolean[] enabled;

    private int count;
    private LayoutInflater inflater;
    private float gridwidth;
    private int COL_NO = Config.getColNumbers();
    private int COL_SPACING = 10;
    private int MARGIN = 8;
    private float density;
    private Context context;


    GridAdapter(ArrayList<View> list, Context context, DisplayMetrics metrics, byte[] randomNumbers){
        super();
        this.list = list;
        this.count = list.size();
        this.enabled = new boolean[count];

        //Sets all tiles in the parallel enabled array to true
        for (int i = 0; i < count; ++i)
            enabled[i] = true;

        //Sets the tiles in the parallel enabled array to false
        // based off of the positions in the past random numbers array
        for(byte val : randomNumbers)
            enabled[val] = false;

        this.inflater = LayoutInflater.from(context);

        //Sets the tile width
        if(metrics.widthPixels < metrics.heightPixels)
            this.gridwidth = Math.round(((metrics.widthPixels/metrics.density) - (COL_SPACING * (COL_NO - 1)) - (MARGIN * 2))/COL_NO);
        else
            this.gridwidth = Math.round(((metrics.heightPixels/metrics.density) - (COL_SPACING * (COL_NO - 1)) - (MARGIN * 2) - 75)/COL_NO);

        //Determines the pixel density of the device
        this.density = metrics.density;
        this.context = context;
    }

    /**
     * Returns the number of elements in the grid
     * @return count
     */
    @Override
    public int getCount() {
        return count;
    }

    /**
     * Returns a view at the passed position
     * @param position Position in the array for the view
     * @return Grid tile
     */
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    /**
     * This is a useless function i had to
     * @param position
     * @return the passed parameter
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Inflates the grid tiles
     * @param position the position of the view object in the grid's array list
     * @param view the item in grid being passed for inflation
     * @param viewGroup used for inflating
     * @return the inflated view object
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = inflater.inflate(R.layout.grid_tile, viewGroup, false);

            view.getLayoutParams().height = (int) (gridwidth*density);
            view.getLayoutParams().width = (int) (gridwidth*density);

            ColorDrawable drawableColor = (ColorDrawable) list.get(position).getBackground();

            if(drawableColor != null) {
                int color = drawableColor.getColor();
                view.setBackgroundColor(color);
            }
        }
        return view;
    }

    /**
     * @param position the passed grid tile
     * @return the boolean value of if the tile is enabled
     */
    @Override
    public boolean isEnabled(int position){
        return enabled[position];
    }
}
