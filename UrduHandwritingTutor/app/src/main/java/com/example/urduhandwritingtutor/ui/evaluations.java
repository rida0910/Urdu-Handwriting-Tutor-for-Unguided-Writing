package com.example.urduhandwritingtutor.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import java.util.List;

public class evaluations extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_evaluations, container, false);
        final ListView listView = root.findViewById(R.id.EvaluationsList);
        MyDbHandler db = new MyDbHandler(getActivity(), "UrduHandwritingTutor.db", null, 1);
        final List<evaluation_class> evals = db.getEvaluations();

        //listView.setAdapter(customListView);
        final CustomAdapter adapter = new CustomAdapter(getActivity(), evals);
        listView.setAdapter(adapter);

        //add listener to listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(root.getContext(),"clicked item:"+i+" "+evals.getCharacters().get(i).toString(),Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();;
                Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                bundle.putInt("Score", evals.get(i).getScore());
                bundle.putString("Feedback", evals.get(i).getFeedback());
                bundle.putString("ComingFrom", "evaluations");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, final int position, long id) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                MyDbHandler db = new MyDbHandler(getActivity(), "UrduHandwritingTutor.db", null, 1);
                                db.deleteEvaluation(evals.get(position).getId());
                                evals.remove(position);
                                adapter.notifyDataSetChanged();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to delete this evaluation?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                return false;
            }
        });

        return root;
    }
}