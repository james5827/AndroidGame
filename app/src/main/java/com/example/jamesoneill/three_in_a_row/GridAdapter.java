package com.example.jamesoneill.three_in_a_row;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<View> list;
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

        for (int i = 0; i < count; ++i)
            enabled[i] = true;

        for(byte val : randomNumbers)
            enabled[val] = false;

        this.inflater = LayoutInflater.from(context);

        if(metrics.widthPixels < metrics.heightPixels)
            this.gridwidth = Math.round(((metrics.widthPixels/metrics.density) - (COL_SPACING * (COL_NO - 1)) - (MARGIN * 2))/COL_NO);
        else
            this.gridwidth = Math.round(((metrics.heightPixels/metrics.density) - (COL_SPACING * (COL_NO - 1)) - (MARGIN * 2) - 75)/COL_NO);

        this.density = metrics.density;
        this.context = context;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

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

    @Override
    public boolean isEnabled(int position)
    {
        return enabled[position];
    }
}
