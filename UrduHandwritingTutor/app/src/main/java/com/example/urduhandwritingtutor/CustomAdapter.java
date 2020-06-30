package com.example.urduhandwritingtutor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    Context context;
    List<evaluation_class> evaluations;

    public CustomAdapter(Context context, List<evaluation_class> evaluations) {
        this.context = context;
        this.evaluations = evaluations;
    }

    @Override
    public int getCount() {
        return evaluations.size();
    }

    @Override
    public Object getItem(int position) {
        return evaluations.get(position).getCharacter();
    }

    @Override
    public long getItemId(int position) {
        return evaluations.get(position).getId();
    }

    /* private view holder class */
    private class ViewHolder {
        TextView character;
        TextView characterName;
        TextView Score;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list_item, null);
            holder = new ViewHolder();

            holder.character = convertView
                    .findViewById(R.id.charImg);
            holder.characterName = convertView
                    .findViewById(R.id.charNametxt);
            holder.Score = convertView.findViewById(R.id.Scoretxt);

            Typeface urdu = Typeface.createFromAsset(context.getAssets(), "Jameel Noori Nastaleeq.ttf");
            holder.character.setTypeface(urdu);


            Integer s = evaluations.get(position).getScore();

            holder.character.setText(evaluations.get(position).getCharacter().split(" ")[0]);
            holder.characterName.setText(evaluations.get(position).getCharacter().split(" ")[1]);
            holder.Score.setText("Score: " + evaluations.get(position).getScore() + "%");

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
