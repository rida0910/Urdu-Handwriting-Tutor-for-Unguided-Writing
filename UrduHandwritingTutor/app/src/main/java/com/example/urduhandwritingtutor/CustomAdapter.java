package com.example.urduhandwritingtutor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context context;
    evaluation_class evaluations;

    public CustomAdapter(Context context, evaluation_class evaluations) {
        this.context = context;
        this.evaluations = evaluations;
    }

    @Override
    public int getCount() {
        return evaluations.Characters.size();
    }

    @Override
    public Object getItem(int position) {
        return evaluations.Characters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return evaluations.Characters.indexOf(getItem(position));
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


            Integer s = evaluations.Score.get(position);

            holder.character.setText(evaluations.Characters.get(position).split(" ")[0]);
            holder.characterName.setText(evaluations.Characters.get(position).split(" ")[1]);
            holder.Score.setText("Score: " + evaluations.Score.get(position) + "%");

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
