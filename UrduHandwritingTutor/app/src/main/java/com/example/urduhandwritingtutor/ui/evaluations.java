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

import com.example.urduhandwritingtutor.CustomAdapter;
import com.example.urduhandwritingtutor.FeedbackActivity;
import com.example.urduhandwritingtutor.MyDbHandler;
import com.example.urduhandwritingtutor.R;
import com.example.urduhandwritingtutor.evaluation_class;

public class evaluations extends Fragment {



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_evaluations, container, false);

        ListView listView = root.findViewById(R.id.EvaluationsList);
        MyDbHandler db = new MyDbHandler(getActivity(), "UrduHandwritingTutor.db", null, 1);
        final evaluation_class evals = db.getEvaluations();

        //listView.setAdapter(customListView);
        CustomAdapter adapter = new CustomAdapter(getActivity(), evals);
        listView.setAdapter(adapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(root.getContext(),"clicked item:"+i+" "+evals.getCharacters().get(i).toString(),Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();;
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                bundle.putInt("Score", evals.getScore().get(i));
                bundle.putString("ComingFrom", "evaluations");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        return root;
    }
}