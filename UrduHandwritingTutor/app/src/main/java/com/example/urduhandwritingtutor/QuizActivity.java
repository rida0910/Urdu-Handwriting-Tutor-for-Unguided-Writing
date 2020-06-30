package com.example.urduhandwritingtutor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    CanvasView canvasView;
    MyDbHandler db;
    TextView charText;
    Button nextBtn;
    String character;
    List<String> characters;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        canvasView = (CanvasView)findViewById(R.id.canvas);
        db = new MyDbHandler(this, "UrduHandwritingTutor.db", null, 1);
        charText = findViewById(R.id.chartext);
        nextBtn = findViewById(R.id.evaluate);

        List<evaluation_class> evaluations = db.getEvaluations();

        List<String> characters = new ArrayList<>();

        for (int i = 0; i < evaluations.size(); i++)
        {
            characters.add(evaluations.get(i).getCharacter());
        }

        HashSet<String> charactersSet = new HashSet<>(characters);
        if (charactersSet.size() < 5)
        {
            charText.setText("You need to learn atleast 5 characters to unlock this feature!");
            nextBtn.setVisibility(View.GONE);
        }
        else
        {
            characters = new ArrayList<>(charactersSet);
            Random rand = new Random();
            character = characters.get(rand.nextInt(characters.size()));
            charText.setText("Write " + character);
            characters.remove(character);
            i = 2;
        }
    }

    public void next(View view)
    {

        i++;
        Random rand = new Random();
        character = characters.get(rand.nextInt(characters.size()));
        charText.setText("Write " + character);
        characters.remove(character);
        if (i == 4)
        {
            nextBtn.setText("Done");
        }
    }

    public void clearCanvas(View view)
    {
        canvasView.clearCanvas();
    }
}
