package com.example.urduhandwritingtutor;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;


public class CustomAdapterQuizResults  extends BaseAdapter {

    Context context;
    List<Quiz_class> quizzes;

    public CustomAdapterQuizResults (Context context, List<Quiz_class> quizzes) {
        this.context = context;
        this.quizzes = quizzes;
    }

    @Override
    public int getCount() {
        return quizzes.size();
    }

    @Override
    public Object getItem(int position) {
        return quizzes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return quizzes.get(position).getQuizID();
    }

    /* private view holder class */
    private class ViewHolder {
        TextView QuizNumber;
        TextView Score;
        RatingBar ratingBar;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.custom_list_quiz, null);
            holder = new ViewHolder();

            holder.QuizNumber = convertView
                    .findViewById(R.id.quizNumber);

            holder.Score = convertView.findViewById(R.id.Scoretxt);
            holder.ratingBar = convertView.findViewById(R.id.rating);


            int score = 0;

            for (evaluation_class evals : quizzes.get(position).evaluations)
            {
                score = score + evals.getScore();
            }

            score = score * 100/500;

            holder.QuizNumber.setText("Quiz " + quizzes.get(position).getQuizNumber());
            holder.Score.setText("Total Score: " + score + "%");
            holder.ratingBar.setRating(score/20);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

}
