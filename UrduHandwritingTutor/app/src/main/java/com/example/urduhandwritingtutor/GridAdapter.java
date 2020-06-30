package com.example.urduhandwritingtutor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridAdapter  extends BaseAdapter {

    Context context;
    Quiz_class quiz;

    public GridAdapter (Context context, Quiz_class quizzes) {
        this.context = context;
        this.quiz = quizzes;
    }

    @Override
    public int getCount() {
        return quiz.evaluations.size();
    }

    @Override
    public Object getItem(int position) {
        return quiz.getEvaluations().get(position);
    }

    @Override
    public long getItemId(int position) {
        return quiz.evaluations.get(position).getId();
    }

    /* private view holder class */
    private class ViewHolder {
        TextView character;
        TextView Score;
        TextView Feedback;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_quiz_feedback_item, null);
            holder = new ViewHolder();

            holder.character = convertView
                    .findViewById(R.id.charactertxt);

            holder.Score = convertView.findViewById(R.id.scoretextview);
            holder.Feedback = convertView.findViewById(R.id.feedback);

            holder.character.setText(quiz.evaluations.get(position).getCharacter());
            holder.Score.setText(String.valueOf(quiz.evaluations.get(position).getScore()));
            holder.Feedback.setText(quiz.evaluations.get(position).getFeedback());

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}

