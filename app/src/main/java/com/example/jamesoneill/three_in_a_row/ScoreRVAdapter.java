package com.example.jamesoneill.three_in_a_row;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ScoreRVAdapter extends RecyclerView.Adapter<ScoreRVAdapter.ViewHolder> {

    private Context context;
    private List<Score> items;

    ScoreRVAdapter(Context context, List<Score> items){
        Log.i("scores", "Constructor: " + items.toString());
        this.context = context;
        this.items = items;
    }

    @Override
    public ScoreRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(this.context);
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        Log.i("scores", "onCreateViewHolder: ");
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreRVAdapter.ViewHolder holder, int position) {
        final Score score = this.items.get(position);
        Log.i("scores", "onBindViewHolder: " + score.toString());
        holder.text.setText((++position) + " " + score.getName() + " " + score.getTime() + " Seconds Left");
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public View view;

        ViewHolder(View itemView){
            super(itemView);
            this.text = itemView.findViewById(R.id.ScoreText);
            this.view = itemView;
            Log.i("scores", "ViewHolder: ");
        }
    }
}
