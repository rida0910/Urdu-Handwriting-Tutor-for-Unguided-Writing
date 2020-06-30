package com.example.urduhandwritingtutor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.urduhandwritingtutor.CustomAdapterQuizResults;
import com.example.urduhandwritingtutor.MyDbHandler;
import com.example.urduhandwritingtutor.QuizFeedback;
import com.example.urduhandwritingtutor.Quiz_class;
import com.example.urduhandwritingtutor.R;

import java.util.List;

public class QuizListDisplay extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_evaluations, container, false);

        ListView listView = root.findViewById(R.id.EvaluationsList);
        MyDbHandler db = new MyDbHandler(getActivity(), "UrduHandwritingTutor.db", null, 1);

        final List<Quiz_class> quizzes = db.getQuizzes();

        //listView.setAdapter(customListView);
        CustomAdapterQuizResults adapter = new CustomAdapterQuizResults(getActivity(), quizzes);
        listView.setAdapter(adapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(root.getContext(),"clicked item:"+i+" "+evals.getCharacters().get(i).toString(),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), QuizFeedback.class);
                intent.putExtra("Quiz", quizzes.get(i));
                startActivity(intent);
            }
        });


        return root;
    }
}