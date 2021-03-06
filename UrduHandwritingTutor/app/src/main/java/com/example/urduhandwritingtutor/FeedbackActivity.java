package com.example.urduhandwritingtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.urduhandwritingtutor.ui.evaluations;

public class FeedbackActivity extends AppCompatActivity {

    Bundle bundle;
    String stuff;
    String character;
    int i;
    Button nextbtn;
    TextView scoretxt;
    RatingBar ratingBar;
    TextView feedbacktxt;
    Button CompleteBtn;
    String ComingFrom;
    TextView characterTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        bundle = getIntent().getExtras();
        scoretxt = (TextView)findViewById(R.id.scoretxt);
        ratingBar = findViewById(R.id.rating);
        int score = bundle.getInt("Score");
        scoretxt.setText(String.valueOf(score));
        feedbacktxt = findViewById(R.id.feedback);
        feedbacktxt.setText(bundle.getString("Feedback"));
        ratingBar.setRating(score/20);
        nextbtn = (Button)findViewById(R.id.nextbtn);
        CompleteBtn = findViewById(R.id.completebtn);
        characterTextView = findViewById(R.id.charactertextview);

        ComingFrom = bundle.getString("ComingFrom");
        if (ComingFrom.equals("evaluations"))
        {
            nextbtn.setVisibility(View.GONE);
            CompleteBtn.setText("Go Back");

        }
        else {
            stuff = bundle.getString("character");
            characterTextView.setText(stuff);
            i = bundle.getInt("Number");
            if (i == 37) {
                nextbtn.setVisibility(View.GONE);
            }
        }

    }

    public void next(View view)
    {
        Intent intent = new Intent(FeedbackActivity.this, PracticeActivity.class);
        i++;
        bundle.putString("character", getCharacter(i));
        bundle.putInt("Number", i);
        intent.putExtras(bundle);
        startActivity(intent);
        FeedbackActivity.this.finish();
    }

    public void complete(View view)
    {
        if (ComingFrom == "TestActivity") {
            Intent intent = new Intent(FeedbackActivity.this, MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else if (ComingFrom == "evaluations")
        {
            Fragment fragment = new evaluations();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        }
        FeedbackActivity.this.finish();

    }

    public String getCharacter(int i)
    {
        if (i == 1)
        {
            character = "ا Alif";
        }
        else if (i == 2)
        {
            character = "ب Bay";
        }
        else if (i == 3)
        {
            character = "پ Pay";
        }
        else if (i == 4)
        {
            character = "ت Tay";
        }
        else if (i == 5)
        {
            character = "ٹ TTay";
        }
        else if (i == 6)
        {
            character = "ث Say";
        }
        else if (i == 7)
        {
            character = "ج Jeem";
        }
        else if (i == 8)
        {
            character = "چ Chay";
        }
        else if (i == 9)
        {
            character = "ح Hay";
        }
        else if (i == 10)
        {
            character = "خ Khay";
        }
        else if (i == 11)
        {
            character = "د Daal";
        }
        else if (i == 12)
        {
            character = "ڈ DDal";
        }
        else if (i == 13)
        {
            character = "ذ Zaal";
        }
        else if (i == 14)
        {
            character = "ر Ray";
        }
        else if (i == 15)
        {
            character = "ڑ RRay";
        }
        else if (i == 16)
        {
            character = "ز Zay";
        }
        else if (i == 17)
        {
            character = "ژ SSay";
        }
        else if (i == 18)
        {
            character = "س Seen";
        }
        else if (i == 19)
        {
            character = "ش Sheen";
        }
        else if (i == 20)
        {
            character = "ص Suaad";
        }
        else if (i == 21)
        {
            character = "ض Zuaad";
        }
        else if (i == 22)
        {
            character = "ط Toayein";
        }
        else if (i == 23)
        {
            character = "ظ Zoayein";
        }
        else if (i == 24)
        {
            character = "ع Aayein";
        }
        else if (i == 25)
        {
            character = "غ Ghayein";
        }
        else if (i == 26)
        {
            character = "ف Fay";
        }
        else if (i == 27)
        {
            character = "ق Qaaf";
        }
        else if (i == 28)
        {
            character = "ک Kaaf";
        }
        else if (i == 29)
        {
            character = "گ Gaaf";
        }
        else if (i == 30)
        {
            character = "ل Laam";
        }
        else if (i == 31)
        {
            character = "م Meem";
        }
        else if (i == 32)
        {
            character = "ن Noon";
        }
        else if (i == 33)
        {
            character = "و Wao";
        }
        else if (i == 34)
        {
            character = "ہ Haa";
        }
        else if (i == 35)
        {
            character = "ء Hamza";
        }
        else if (i == 36)
        {
            character = "ی Choti yay";
        }
        else if (i == 37)
        {
            character = "ے Barri yay";
        }
        return character;
    }

}
