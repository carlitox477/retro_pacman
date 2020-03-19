package com.example.pacman;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ScoreAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Score> scores;

    public  ScoreAdapter(Context context, ArrayList<Score> scores){
        this.context=context;
        this.scores=scores;
    }

    @Override
    public int getCount() {
        return this.scores.size();
    }

    @Override
    public Score getItem(int position) {
        return this.scores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Score item=this.scores.get(position);
        convertView= LayoutInflater.from(this.context).inflate(R.layout.layout_score,null);
        TextView tvPosition = (TextView) convertView.findViewById(R.id.tv_position);
        TextView tvNickname=(TextView) convertView.findViewById(R.id.tv_nickname);
        TextView tvScore=(TextView) convertView.findViewById(R.id.tv_score);

        tvPosition.setText(""+(position+1));
        tvNickname.setText(item.getNickname());
        tvScore.setText(item.getScoreString());
        return convertView;
    }
}
