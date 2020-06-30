package com.example.urduhandwritingtutor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TestActivity extends AppCompatActivity {

    CanvasView canvasView;
    String stuff;
    Bundle bundle;
    MyDbHandler db;
    TextView charText;
    String ComingFrom;
    Button nextBtn;
    List<String> characters;
    String character;
    int num;
    long QuizId;
    ImageButton clrBtn;
    TextView TimerTxt;
    int w, h;
    int alpha = 0;
    int beta = 0;
    int gamma = 0;
    private TensorFlowInferenceInterface inferenceInterface;
    CountDownTimer countDownTimer;
    int counter = 30;
    List<Float> ResampledX = new ArrayList<>();
    List<Float> ResampledY = new ArrayList<>();
    List<Float> ResampledX_dot = new ArrayList<>();
    List<Float> ResampledY_dot = new ArrayList<>();
    boolean isWrongstarting = true, isWrongEnding = true, isWrongInTheMiddle = true;
    List<evaluation_class> evaluations = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        canvasView = (CanvasView)findViewById(R.id.canvas);
        charText = findViewById(R.id.chartext);
        db = new MyDbHandler(this, "UrduHandwritingTutor.db", null, 1);
        bundle = getIntent().getExtras();
        ComingFrom = bundle.getString("ComingFrom");
        nextBtn = findViewById(R.id.evaluate_test);
        clrBtn = findViewById(R.id.clearbtn);
        TimerTxt = findViewById(R.id.timer);
        TimerTxt.setText("30s");

        if (ComingFrom!= null && ComingFrom.equals("PracticeActivity"))
        {
            TimerTxt.setVisibility(View.GONE);
            stuff = bundle.getString("character");
            charText.setText("Test: " + stuff);
        }
        else
        {
            List<evaluation_class> evaluations = db.getEvaluations();

            List<String> charactersList = new ArrayList<>();

            for (int i = 0; i < evaluations.size(); i++)
            {
                charactersList.add(evaluations.get(i).getCharacter());
            }

            HashSet<String> charactersSet = new HashSet<>(charactersList);
            if (charactersSet.size() < 5)
            {
                charText.setText("You need to learn atleast 5 characters to unlock this feature!");
                charText.setTextSize(35);
                charText.setBackgroundColor(Color.WHITE);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) charText.getLayoutParams();
                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
                charText.setLayoutParams(lp);
                nextBtn.setVisibility(View.GONE);
                canvasView.setVisibility(View.GONE);
                clrBtn.setVisibility(View.GONE);
                TimerTxt.setVisibility(View.GONE);
            }
            else
            {
                QuizId = db.addQuiz();
                countDownTimer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        TimerTxt.setText(String.valueOf(counter --)+ "s");
                    }

                    @Override
                    public void onFinish() {
                        Intent intent = new Intent(TestActivity.this, MainActivity.class);
                        startActivity(intent);
                        TestActivity.this.finish();
                    }
                }.start();

                canvasView.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.boardcropped));
                characters = new ArrayList<>(charactersSet);
                Random rand = new Random();
                character = characters.get(rand.nextInt(characters.size()));
                charText.setText("Write " + character);
                characters.remove(character);
                num = 1;
            }
        }
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), "ANN.pb");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void done(View view)
    {
        if (canvasView.getListX().size() == 0)
        {
            Toast.makeText(this,  "Please write the specified character!",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            List<Float> listX = new ArrayList<>();
            List<Float> listY = new ArrayList<>();
            List<Long> listT = new ArrayList<>();
            //db.addStrokeData(cv);
            for (Float f : canvasView.listX)
            {
                listX.add(f);
            }
            for (Float f : canvasView.listY)
            {
                listY.add(f);
            }
            for (Long f : canvasView.TimeList)
            {
                listT.add(f);
            }

            h = canvasView.getHeight();
            w = canvasView.getWidth();

            if (ComingFrom!= null && ComingFrom.equals("PracticeActivity")) {
                int i = bundle.getInt("Number");
                float[] floatArray = PreprocessData(listX, listY, listT, w, h, i);
                float[] output = predict(floatArray);

                List<Float> result = new ArrayList<>();
                for (float f : output) {
                    result.add(f);
                }

                float confidence = result.get(i);
                int score = Math.round(confidence*100);
                //String Feedback = "Number of Strokes: Correct\n\nOrder of Strokes: correct\n\nStroke Direction: Correct";
                String Feedback;
                float confidenceMax = Collections.max(result);
                int indexOfMax = result.indexOf(confidenceMax);
                if (!getCharacter(indexOfMax).equals(getCharacter(i)))
                {
                    Feedback = "You wrote " + getCharacter(indexOfMax) + " instead of " + getCharacter(i);
                    score = 0;
                }
                else {
                    Feedback = getFeedback(canvasView, i);
                    score = (score / 100) * 40 + alpha + beta + gamma;
                }
                String hh = getCharacter(i);
                db.addevaluation(getCharacter(i), score, Feedback);
                bundle.putInt("Score", score);
                bundle.putString("ComingFrom", "TestActivity");
                bundle.putString("Feedback", Feedback);
                Intent intent = new Intent(TestActivity.this, FeedbackActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                TestActivity.this.finish();
            }
            else
            {
                float[] floatArray = PreprocessData(listX, listY, listT, w, h, getCharacterNumber(character));
                float[] output = predict(floatArray);

                List<Float> result = new ArrayList<>();
                for (float f : output) {
                    result.add(f);
                }

                float confidence = result.get(getCharacterNumber(character));
                String Feedback = getFeedback(canvasView, getCharacterNumber(character));
                int score = (Math.round(confidence*100)/100) * 40;
                score = score + alpha + beta + gamma;
                long evalID = db.addevaluation(character, score, Feedback);
                db.addQuizResults(QuizId, evalID);
                evaluation_class evaluation = new evaluation_class((int)evalID, character, score, Feedback);
                evaluations.add(evaluation);
                if (num < 5) {
                    Random rand = new Random();
                    character = characters.get(rand.nextInt(characters.size()));
                    charText.setText("Write " + character);
                    characters.remove(character);
                }
                if (num == 4)
                {
                    nextBtn.setText("Done");

                }
                num++;
            }
        }
        canvasView.clearCanvas();
        if (num == 6)
        {
            countDownTimer.cancel();
            Quiz_class quiz = new Quiz_class((int)QuizId, db.getQuizNumber((int)QuizId), evaluations);
            Intent intent = new Intent(TestActivity.this, QuizFeedback.class);
            intent.putExtra("Quiz", quiz);
            startActivity(intent);
            TestActivity.this.finish();
        }
    }

    class XYCoord {
        List<Float> XCoord = new ArrayList<>();
        List<Float> YCoord = new ArrayList<>();
    }

    public double distance(Float x1, Float x2, Float y1, Float y2)
    {
        Float x = x1 - x2;
        Float y = y1 - y2;
        double dist = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        return dist;
    }

    public double FindAvgSpacing(List<Float> X, List<Float> Y)
    {
        double sum = 0;
        for(int i = 0; i < X.size()-1; i++)
        {
            sum = sum + distance(X.get(i), X.get(i+1), Y.get(i), Y.get(i+1));
        }
        double avg = sum/X.size()-1;
        return avg;
    }


    public XYCoord Resample(List<Float> X, List<Float> Y, int count)
    {
        XYCoord XYCoord = new XYCoord();

        float[] X_array = ListToArray(X);
        float[] Y_array = ListToArray(Y);
        if (X_array.length == 1 || X_array.length == count)
        {
            XYCoord.XCoord = X;
            XYCoord.YCoord = Y;
            return XYCoord;
        }

        if (X.size() < count)
        {
            XYCoord.XCoord = X;
            XYCoord.YCoord = Y;
            while (XYCoord.XCoord.size() < count)
            {
                XYCoord.XCoord.add(X.get(X.size() - 1));
                XYCoord.YCoord.add(Y.get(Y.size() - 1));
            }
            return XYCoord;
        }

        double t = 5;


        List<Float> Resampled_X = new ArrayList<>();
        List<Float> Resampled_Y = new ArrayList<>();
        Resampled_X.add(X.get(0));
        Resampled_Y.add(Y.get(0));

        int j = (X.size()/count) + 1;
        int i = j;

        while (Resampled_X.size() < count - 2 && i + j < X.size() - 1)
        {
            if (distance(X.get(i), X.get(i + j), Y.get(i), Y.get(i + j)) >= t)
            {
                Resampled_X.add(X.get(i));
                Resampled_Y.add(Y.get(i));
            }
            i = i + j;
        }
        Resampled_X.add(X.get(X.size() - 1));
        Resampled_Y.add(Y.get(Y.size() - 1));


        while (Resampled_X.size() <= count)
        {
            Resampled_X.add(X.get(X.size() - 1));
            Resampled_Y.add(Y.get(Y.size() - 1));
        }


        XYCoord.XCoord = Resampled_X.subList(0, count);
        XYCoord.YCoord = Resampled_Y.subList(0, count);
        return XYCoord;
    }


    public XYCoord Smooth(List<Float> X, List<Float> Y)
    {
        XYCoord XYCoord = new XYCoord();

        if (X.size() < 5)
        {
            XYCoord.XCoord = X;
            XYCoord.YCoord = Y;
        }
        else
        {
            List<Float> SmoothX = new ArrayList<>();
            List<Float> SmoothY = new ArrayList<>();
            SmoothX.add(X.get(0));
            SmoothY.add(Y.get(0));
            SmoothX.add(X.get(1));
            SmoothY.add(Y.get(1));

            for (int i = 2; i < X.size() - 2; i++)
            {
                Float xm = (X.get(i-2) + X.get(i-1) + X.get(i) + X.get(i+1) + X.get(i+2))/5;
                Float ym = (Y.get(i-2) + Y.get(i-1) + Y.get(i) + Y.get(i+1) + Y.get(i+2))/5;
                SmoothX.add(xm);
                SmoothY.add(ym);
            }
            SmoothX.add(X.get(X.size() - 2));
            SmoothY.add(Y.get(Y.size() - 2));
            SmoothX.add(X.get(X.size() - 1));
            SmoothY.add(Y.get(Y.size() - 1));

            XYCoord.XCoord = SmoothX;
            XYCoord.YCoord = SmoothY;
        }

        return XYCoord;
    }


    public List<Float> normalize(List<Float> X, int factor) {
        List<Float> normalized_list = new ArrayList<>();
        for (int i = 0; i < X.size(); i++) {
            normalized_list.add(X.get(i) / factor);
        }
        return normalized_list;
    }

    private float[] predict(float[] input){
        // model has 38 output neurons
        float output[] = new float[38];

        // feed network with input of shape (1,input.length) = (1,2)
        inferenceInterface.feed("dense_4_input:0", input, 1, input.length);
        inferenceInterface.run(new String[]{"dense_6/Softmax:0"});
        inferenceInterface.fetch("dense_6/Softmax:0", output);

        // return prediction
        return output;
    }

    float[] PreprocessData(List<Float> listX, List<Float> listY, List<Long> listT, int w, int h, int character)
    {
        //getting the dots and their indices
        List<Long> dots_time = new ArrayList<>();
        List<Integer> dots_indices = new ArrayList<>();
        Long dot;
        int first_dot_index;

        int i = 0;
        while (i < listT.size() - 1)
        {
            if (listT.get(i) >= listT.get(i+1))
            {
                dot = listT.get(i+1);
                dots_time.add(dot);
                dots_indices.add(i+1);
            }
            i = i + 1;
        }


        XYCoord SmoothedXY = new XYCoord();
        if (dots_indices.size() != 0 && character != 1 && character != 9 && character != 11 &&
                character != 14 && character != 18 && character != 20 && character != 22 &&
                character != 24 && character != 30 && character != 31 && character != 33 &&
                character != 34 && character != 35 && character != 36 && character != 37)
        {
            List<Float> listX_withoutDot = new ArrayList<>();
            List<Float> listY_withoutDot = new ArrayList<>();
            List<Float> listX_Dot = new ArrayList<>();
            List<Float> listY_Dot = new ArrayList<>();
            first_dot_index = dots_indices.get(0);

            //Separating dot and basic stroke
            listX_withoutDot.addAll(listX.subList(0, first_dot_index));
            listY_withoutDot.addAll(listY.subList(0, first_dot_index));
            listX_Dot.addAll(listX.subList(first_dot_index, listX.size()));
            listY_Dot.addAll(listY.subList(first_dot_index, listY.size()));

            //if (listX_Dot.size() > listX_withoutDot.size() && character != 5 && character != 12 && character != 15 && character != 28 && character != 29)
            if (!checkStrokeOrder(listX, listY, listT, character))
            {
                List<Float> tempX;
                List<Float> tempY;

                tempX = listX_Dot;
                tempY = listY_Dot;

                listX_Dot = listX_withoutDot;
                listY_Dot = listY_withoutDot;

                listX_withoutDot = tempX;
                listY_withoutDot = tempY;

            }


            XYCoord XYCoord = new XYCoord();

            //if (listX_Dot.size() > 6)
            if (character == 5 || character == 12 || character == 15 || character == 28 || character == 29)
            {
                XYCoord = Resample(listX_Dot, listY_Dot, 20);
                ResampledX_dot.addAll(XYCoord.XCoord);
                ResampledY_dot.addAll(XYCoord.YCoord);
            }
            else if (dots_indices.size() <= 3 && dots_indices.size() != 1)
            {
                for (int j = 0; j < dots_indices.size(); j++)
                {
                    ResampledX_dot.add(listX.get(dots_indices.get(j)));
                    ResampledY_dot.add(listY.get(dots_indices.get(j)));
                }
            }
            else
            {
                XYCoord = Resample(listX_Dot, listY_Dot, 3);
                ResampledX_dot = XYCoord.XCoord;
                ResampledY_dot = XYCoord.YCoord;
            }

            XYCoord = Resample(listX_withoutDot, listY_withoutDot, 50-ResampledX_dot.size());
            ResampledX = XYCoord.XCoord;
            ResampledY = XYCoord.YCoord;


            if (ResampledX.size() > 50-ResampledX_dot.size())
            {
                ResampledX = ResampledX.subList(0, 51-ResampledX_dot.size());
                ResampledY = ResampledY.subList(0, 51-ResampledX_dot.size());
            }

            float[] ResampledX_dot_array = ListToArray(ResampledX_dot);

            SmoothedXY = Smooth(ResampledX, ResampledY);

            for (Float f : ResampledX_dot_array)
            {
                SmoothedXY.XCoord.add(f);
            }

            float[] ResampledY_dot_array = ListToArray(ResampledY_dot);
            for (Float f : ResampledY_dot_array)
            {
                SmoothedXY.YCoord.add(f);
            }

            //ResampledX = Final_X_Coord;
            //ResampledY = Final_Y_Coord;
        }

        else
        {
            XYCoord XYCoord;
            XYCoord = Resample(listX, listY, 50);
            ResampledX = XYCoord.XCoord;
            ResampledY = XYCoord.YCoord;
            SmoothedXY = Smooth(ResampledX, ResampledY);
        }

        List<Float> Normalized_X = normalize(SmoothedXY.XCoord, w);
        List<Float> Normalized_Y = normalize(SmoothedXY.YCoord, h);

        List<Float> CombinedXY = new ArrayList<>(Normalized_X.subList(0, Normalized_X.size()));
        CombinedXY.addAll(Normalized_Y.subList(0, Normalized_Y.size()));
        CombinedXY.add((float)dots_indices.size());

        float[] floatArray = new float[CombinedXY.size()];
        int k = 0;

        for (Float f :CombinedXY) {
            floatArray[k++] = (f != null ? f : (float)0.9); // Or whatever default you want.
        }
        return floatArray;
    }

    String getFeedback(CanvasView canvasView, int character)
    {
        String Feedback;

        String numStrokes, OrderofStrokes, Direction = "";
        OrderofStrokes = "";

        //no. of strokes
        if (getNumofStrokes(canvasView.TimeList).size() == getCorrectnumofStrokes(character))
        {
            numStrokes = "Number of Strokes: " + getNumofStrokes(canvasView.TimeList).size() + " (Correct)";
            alpha = 20;
        }
        else
        {
            if (getCorrectnumofStrokes(character) == 1)
            {
                numStrokes = "This character should be written in a single stroke";
            }
            else
            {
                numStrokes = "This character should be written in " + getCorrectnumofStrokes(character) + " strokes";
            }
        }

        //now for order of strokes
        if (checkStrokeOrder(canvasView.getListX(), canvasView.getListY(), canvasView.TimeList, character) && alpha == 20)
        {
            OrderofStrokes = "Order of Strokes: Correct";
            beta = 20;
        }
        else if (character != 1 && character != 9 && character != 11 &&
                character != 14 && character != 18 && character != 20 && character != 22 &&
                character != 24 && character != 30 && character != 31 && character != 33 &&
                character != 34 && character != 35 && character != 36 && character != 37)
        {
            OrderofStrokes = "Base shape must be drawn first";
        }

        if (beta == 20) {
            //now for direction of strokes\
            List<String> directions = getStrokeDirection(canvasView.getListX(), canvasView.getListY());
            Direction = checkStrokeDirection(character, directions);
            if (Direction == "correct"){
                gamma = 20;
            }
            Direction = "Stroke Direction: " + Direction;

        }
        Feedback = numStrokes + "\n\n" + OrderofStrokes + "\n\n" + Direction;

        return Feedback;
    }

    List<String> getStrokeDirection(List<Float> listX, List<Float> listY)
    {
        List<String> directions = new ArrayList<>();
        Float deltaX, deltaY;
        double theta;

        for (int i = 1; i < listX.size() - 1; i++)
        {
            deltaX = listX.get(i) - listX.get(i-1);
            deltaY = listY.get(i) - listY.get(i-1);
            theta = Math.toDegrees(Math.atan2((double) -deltaY, (double) deltaX));
            if (theta < 0.0) {
                theta += 360.0;
            }

            if (theta > 5.0 && theta < 85.0)
            {
                directions.add("up right");
            }
            else if (theta > 85.0 && theta < 95.0)
            {
                directions.add("up");
            }
            else if (theta > 95.0 && theta < 175.0)
            {
                directions.add("up left");
            }
            else if (theta > 175.0 && theta < 185.0)
            {
                directions.add("left");
            }
            else if (theta > 185.0 && theta < 265.0)
            {
                directions.add("down left");
            }
            else if (theta > 265.0 && theta < 275.0)
            {
                directions.add("down");
            }
            else if (theta > 275.0 && theta < 3555.0)
            {
                directions.add("down right");
            }
            else if (theta < 5.0 || theta > 355.0)
            {
                directions.add("right");
            }
        }
        return directions;
    }

    String checkStrokeDirection(int character, List<String> Directions)
    {
        String result = "";

        int i = Directions.size()/3;

        List<String> Start = Directions.subList(0, i);
        List<String> Middle = Directions.subList(i, i+i);
        List<String> End = Directions.subList(i+i, Directions.size());
        int wrongStart = 0;
        int wrongMiddle = 0;
        int wrongEnd = 0;


        if (character == 1)
        {
            if (Directions.contains("up") || Directions.contains("up left") || Directions.contains("up right"))
            {
                result = "Alif should be written in downwards motion";
                return result;
            }
            else
            {
                result = "correct";
                isWrongstarting = false; isWrongEnding = false; isWrongInTheMiddle = false;
                return result;
            }
        }

        else if (character == 2 || character == 3 || character == 4 || character == 6)
        {
            for (String s : Start)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("up left") || s.equals("down right"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("down right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("right") || s.equals("down")|| s.equals("down left") || s.equals("down right") || s.equals("up right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 5 || character == 12 || character == 15)
        {
            List<String> DirectionsPrimary = getStrokeDirection(ResampledX.subList(0, 31), ResampledY.subList(0, 31));
            List<String> DirectionsSecondary = getStrokeDirection(ResampledX_dot, ResampledY_dot);
            i = DirectionsPrimary.size()/3;
            int j = DirectionsSecondary.size()/3;
            int wrongStartPrimary = 0; int wrongMiddlePrimary = 0; int wrongEndPrimary = 0;
            int wrongStartSecondary = 0; int wrongMiddleSecondary = 0; int wrongEndSecondary = 0;

            List<String> StartPrimary = DirectionsPrimary.subList(0, i);
            List<String> MiddlePrimary = DirectionsPrimary.subList(i, i+i);
            List<String> EndPrimary = DirectionsPrimary.subList(i+i, DirectionsPrimary.size());

            List<String> StartSecondary = DirectionsSecondary.subList(0, j);
            List<String> MiddleSecondary = DirectionsSecondary.subList(j, j+j);
            List<String> EndSecondary = DirectionsSecondary.subList(j+j, DirectionsSecondary.size());

            if (character == 5) {
                for (String s : StartPrimary) {
                    if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("up left") || s.equals("down right")) {
                        wrongStartPrimary++;
                    }
                }
                for (String s : MiddlePrimary) {
                    if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("down right")) {
                        wrongMiddlePrimary++;
                    }
                }
                for (String s : EndPrimary) {
                    if (s.equals("down") || s.equals("down left") || s.equals("down right") || s.equals("up right")) {
                        wrongEndPrimary++;
                    }
                }
            }
            if (character == 12) {
                for (String s : StartPrimary) {
                    if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("down") || s.equals("down left") || s.equals("left")) {
                        wrongStartPrimary ++;
                    }
                }
                for (String s : MiddlePrimary) {
                    if (s.equals("up")|| s.equals("up right") || s.equals("up left")) {
                        wrongMiddlePrimary ++;
                    }
                }
                for (String s : EndPrimary) {
                    if (s.equals("down right") || s.equals("up")|| s.equals("up right") || s.equals("up left")) {
                        wrongEndPrimary ++;
                    }
                }
            }
            if (character == 15) {
                for (String s : StartPrimary) {
                    if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right") || s.equals("left")) {
                        wrongStartPrimary ++;
                    }
                }
                for (String s : MiddlePrimary) {
                    if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right") || s.equals("left")) {
                        wrongMiddlePrimary ++;
                    }
                }
                for (String s : EndPrimary) {
                    if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right")) {
                        wrongEndPrimary ++;
                    }
                }
            }
            for (String s : StartSecondary) {
                if (s.equals("up")|| s.equals("up right") || s.equals("right")  || s.equals("down right") || s.equals("left")  || s.equals("up left")) {
                    wrongStartSecondary ++;
                }
            }
            for (String s : MiddleSecondary) {
                if (s.equals("right") || s.equals("left")  || s.equals("up left")) {
                    wrongMiddleSecondary ++;
                }
            }
            for (String s : EndSecondary) {
                if (s.equals("up left") || s.equals("up") || s.equals("up right") || s.equals("right")) {
                    wrongEndSecondary ++;
                }
            }

            if (wrongStartPrimary > 5)
            {
                result = "The starting direction of the primary stroke was wrong";
            }
            else if (wrongMiddlePrimary > 5)
            {
                result = "The direction of the primary stroke went wrong in the middle";
            }
            else if (wrongEndPrimary > 7)
            {
                result = "The ending direction of the primary stroke was wrong";
            }
            else if (wrongStartSecondary > 5)
            {
                result = "The starting direction of the secondary stroke was wrong";
            }
            else if (wrongMiddleSecondary > 5)
            {
                result = "The direction of the secondary stroke went wrong in the middle";
            }
            else if (wrongEndSecondary > 7)
            {
                result = "The ending direction of the secondary stroke was wrong";
            }

            else
            {
                result = "correct";
                isWrongstarting = false; isWrongEnding = false; isWrongInTheMiddle = false;
            }

            return result;
        }

        else if (character == 7 || character == 8 || character == 9 || character == 10)
        {
            for (String s : Start)
            {
                if (s.equals("left")|| s.equals("up left") || s.equals("down right") || s.equals("down"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("up left") || s.equals("left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("left") || s.equals("down left") || s.equals("up left") || s.equals("down right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 11 || character == 13)
        {
            for (String s : Start)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("down") || s.equals("down left") || s.equals("left"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("up left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("down right") || s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 14 || character == 16 || character == 17)
        {
            for (String s : Start)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right") || s.equals("left"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right") || s.equals("left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 18 || character == 19)
        {
            for (String s : Start)
            {
                if (s.equals("up left")|| s.equals("up right") || s.equals("right") || s.equals("down right"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up left")|| s.equals("up right") || s.equals("right") || s.equals("down right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("down") || s.equals("right") || s.equals("down right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 20 || character == 21)
        {
            for (String s : Middle)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("right")  || s.equals("down right")  || s.equals("up left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("down") || s.equals("right") || s.equals("down right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 22 || character == 23)
        {
            for (String s : Start)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("right")  || s.equals("down right") || s.equals("left")  || s.equals("up left"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("left")  || s.equals("up left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("up left") || s.equals("up") || s.equals("up right") || s.equals("right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 24 || character == 25)
        {
            for (String s : Start)
            {
                if (s.equals("up") || s.equals("down right") || s.equals("left"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up") || s.equals("left")  || s.equals("up left") || s.equals("up right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("up left") || s.equals("left") || s.equals("down left") || s.equals("down"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 26)
        {

            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("down right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("right") || s.equals("down")|| s.equals("down left") || s.equals("down right") || s.equals("up right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 27)
        {

            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("right") || s.equals("down")|| s.equals("down left") || s.equals("down right") || s.equals("left"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 28 || character == 29)
        {
            List<String> DirectionsPrimary = getStrokeDirection(ResampledX.subList(0, 31), ResampledY.subList(0, 31));
            List<String> DirectionsSecondary = getStrokeDirection(ResampledX_dot, ResampledY_dot);
            i = DirectionsPrimary.size()/3;

            int wrongStartPrimary = 0; int wrongMiddlePrimary = 0; int wrongEndPrimary = 0;
            int wrongSecondary = 0;

            List<String> StartPrimary = DirectionsPrimary.subList(0, i);
            List<String> MiddlePrimary = DirectionsPrimary.subList(i, i+i);
            List<String> EndPrimary = DirectionsPrimary.subList(i+i, DirectionsPrimary.size());

            for (String s : StartPrimary)
            {
                if (s.equals("up") || s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right"))
                {
                    wrongStartPrimary ++;
                }
            }
            for (String s : MiddlePrimary)
            {
                if (s.equals("right") || s.equals("up right") || s.equals("up") || s.equals("down right") || s.equals("down"))
                {
                    wrongMiddlePrimary ++;
                }
            }
            for (String s : EndPrimary)
            {
                if (s.equals("down") || s.equals("down right") || s.equals("down left"))
                {
                    wrongEndPrimary ++;
                }
            }
            for (String s : DirectionsSecondary)
            {
                if (!s.equals("down left"))
                {
                    wrongSecondary ++;
                }
            }

            if (wrongStartPrimary > 5)
            {
                result = "The starting direction of the primary stroke was wrong";
            }
            else if (wrongMiddlePrimary > 5)
            {
                result = "The direction of the primary stroke went wrong in the middle";
            }
            else if (wrongEndPrimary > 7)
            {
                result = "The ending direction of the primary stroke was wrong";
            }
            else if (wrongSecondary > 5)
            {
                result = "The direction of the secondary stroke was wrong";
            }
            else
            {
                result = "correct";
                isWrongstarting = false; isWrongEnding = false; isWrongInTheMiddle = false;
            }

            return result;

        }

        /*else if (character == 29)
        {
            i = Directions.size()/4;

            List<String> StartPrimary = Directions.subList(0, i);
            List<String> EndPrimary = Directions.subList(i, i+i);
            List<String> Secondary = Directions.subList(i+i, i+i+i);
            List<String> Tertiary = Directions.subList(i+i+i, Directions.size());
            int wrongStartPrimary = 0;
            int wrongEndPrimary = 0;
            int wrongSecondary = 0;
            int wrongTertiary = 0;

            for (String s : StartPrimary)
            {
                if (s.equals("up") || s.equals("up right") || s.equals("up left") || s.equals("right") || s.equals("down right"))
                {
                    wrongStartPrimary ++;
                }
            }
            for (String s : EndPrimary)
            {
                if (s.equals("right") || s.equals("up right") || s.equals("down") || s.equals("down right") || s.equals("down left"))
                {
                    wrongEndPrimary ++;
                }
            }
            for (String s : Secondary)
            {
                if (!s.equals("down left"))
                {
                    wrongSecondary ++;
                }
            }
            for (String s : Tertiary)
            {
                if (!s.equals("down left"))
                {
                    wrongTertiary ++;
                }
            }
            if (wrongStartPrimary > 5)
            {
                result = "The starting direction of the character was wrong";
            }
            else if (wrongEndPrimary > 5)
            {
                result = "The ending direction of the primary stroke was wrong";
            }
            else if (wrongSecondary > 6)
            {
                result = "The direction of the secondary stroke was wrong";
            }
            else if (wrongTertiary > 6)
            {
                result = "The direction of the Tertiary stroke was wrong";
            }

            else
            {
                result = "correct";
                isWrongstarting = false; isWrongEnding = false; isWrongInTheMiddle = false;
            }

            return result;

        }
*/
        else if (character == 30 || character == 32)
        {
            for (String s : Start)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("up left") || s.equals("down right"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("down right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("right") || s.equals("down")|| s.equals("down left") || s.equals("down right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 31)
        {
            for (String s : Middle)
            {
                if (s.equals("right") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("down right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (!s.equals("down") && !s.equals("down left"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 33)
        {

            for (String s : Middle)
            {
                if (s.equals("up")|| s.equals("up right") || s.equals("up left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("down right") || s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 34)
        {
            for (String s : Start)
            {
                if (s.equals("left") || s.equals("up")|| s.equals("up right") || s.equals("up left") || s.equals("right"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up")|| s.equals("left") || s.equals("up left") || s.equals("down left") || s.equals("down"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("up right") || s.equals("right")|| s.equals("down right") || s.equals("down") || s.equals("down left"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 35)
        {
            for (String s : Start)
            {
                if (s.equals("up right") || s.equals("down right") || s.equals("right") || s.equals("down"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up left") || s.equals("up") || s.equals("left") || s.equals("down"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (!s.equals("down left"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 36)
        {
            for (String s : Start)
            {
                if (s.equals("up right") || s.equals("down"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up left") || s.equals("up") || s.equals("up right") || s.equals("down") || s.equals("right"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("down") || s.equals("down left") || s.equals("down right") || s.equals("right") || s.equals("left"))
                {
                    wrongEnd ++;
                }
            }
        }

        else if (character == 37)
        {
            for (String s : Start)
            {
                if (s.equals("up right") || s.equals("up") || s.equals("up left"))
                {
                    wrongStart ++;
                }
            }
            for (String s : Middle)
            {
                if (s.equals("up") || s.equals("up left") || s.equals("left"))
                {
                    wrongMiddle ++;
                }
            }
            for (String s : End)
            {
                if (s.equals("left") ||  s.equals("up left") || s.equals("up") || s.equals("down left") || s.equals("down") || s.equals("down right"))
                {
                    wrongEnd ++;
                }
            }
        }

        if (wrongStart > 5)
        {
            result = "The starting direction of the character was wrong";
        }
        else if (wrongMiddle > 5)
        {
            result = "The direction of the character went wrong in the middle";
        }
        else if (wrongEnd > 7)
        {
            result = "The ending direction of the character was wrong";
        }

        else
        {
            result = "correct";
            isWrongstarting = false; isWrongEnding = false; isWrongInTheMiddle = false;
        }

        return result;
    }



    boolean checkStrokeOrder(List<Float> listX, List<Float> listY, List<Long> TimeList, int character)
    {
        boolean inOrder = true;
        List<Integer> penDowns = getNumofStrokes(TimeList);

        //now lets check the stroke order
        if(penDowns.size() == 1)
        {
            inOrder = true;
        }
        else
        {
            if (character == 5 || character == 12 || character == 15 || character == 28 || character == 29)
            {
                List<Float> FirstStrokeX = new ArrayList<>();
                for (Float f : listX.subList(0, penDowns.get(1)))
                {
                    FirstStrokeX.add(f);
                }
                List<Float> FirstStrokeY = new ArrayList<>();
                for (Float f : listY.subList(0, penDowns.get(1)))
                {
                    FirstStrokeY.add(f);
                }
                List<Long> FirstStrokeT = new ArrayList<>();
                for (Long f : TimeList.subList(0, penDowns.get(1)))
                {
                    FirstStrokeT.add(f);
                }

                float[] floatArray = new float[102];
                if (character == 5 || character == 12 || character == 15)
                {
                    floatArray = PreprocessData(FirstStrokeX, FirstStrokeY, FirstStrokeT, w, h, 22);
                }
                if (character == 28 || character == 29)
                {
                    floatArray = PreprocessData(FirstStrokeX, FirstStrokeY, FirstStrokeT, w, h, 35);
                }
                float[] output = predict(floatArray);

                List<Float> result = new ArrayList<>();
                for (float f : output) {
                    result.add(f);
                }
                float confidence = 0;
                if (character == 5 || character == 12 || character == 15) {confidence = result.get(22);}
                if (character == 28 || character == 29) {confidence = result.get(35);}

                if(Math.round(confidence * 100) >=  2)
                {
                    inOrder = false;
                }
            }

            else
            {
                if (TimeList.subList(0, penDowns.get(1)).size() < TimeList.subList(penDowns.get(1), TimeList.size()-1).size())
                {
                    inOrder = false;
                }
            }
        }

        return inOrder;
    }


    List<Integer> getNumofStrokes(List<Long> TimeList)
    {
        List<Integer> penDowns = new ArrayList<>();
        penDowns.add(0);
        int i = 0;
        while (i < TimeList.size() - 1)
        {
            if (TimeList.get(i) >= TimeList.get(i+1))
            {
                penDowns.add(i+1);
            }
            i = i + 1;
        }
        return penDowns;
    }

    int getCorrectnumofStrokes(int i)
    {
        int num = 1;
        if (i == 2 || i == 5 || i == 7 || i == 10 || i == 12 || i == 13 || i == 15 || i == 16 ||
                i == 21 || i == 23 || i == 25 || i == 26 || i == 28 || i == 32)
        {
            num = 2;
        }
        else if (i == 4 || i == 27 || i == 29)
        {
            num = 3;
        }
        else if (i == 3 || i == 6 || i == 8 || i == 17 || i == 19)
        {
            num = 4;
        }
        return num;
    }

    float[] ListToArray(List<Float> lst)
    {
        float[] floatArray = new float[lst.size()];
        int k = 0;

        for (Float f :lst) {
            floatArray[k++] = f;
        }
        return floatArray;
    }

    List<Float> ArrayToList(float[] array)
    {
        List<Float> result = new ArrayList<>();
        for (float f : array) {
            result.add(f);
        }
        return result;
    }


    public void clearCanvas(View view)
    {
        canvasView.clearCanvas();
    }

    public int getCharacterNumber(String character)
    {
        int i = 0;
        if (character.equals("ا Alif"))
        {
            i = 1;
        }
        else if (character.equals("ب Bay"))
        {
            i = 2;
        }
        else if (character.equals("پ Pay"))
        {
            i = 3;
        }
        else if (character.equals("ت Tay"))
        {
            i = 4;
        }
        else if (character.equals("ٹ TTay"))
        {
            i = 5;
        }
        else if (character.equals("ث Say"))
        {
            i = 6;
        }
        else if (character.equals("ج Jeem"))
        {
            i = 7;
        }
        else if (character.equals("چ Chay"))
        {
            i = 8;
        }
        else if (character.equals("ح Hay"))
        {
            i = 9;
        }
        else if (character.equals("خ Khay"))
        {
            i = 10;
        }
        else if (character.equals("د Daal"))
        {
            i = 11;
        }
        else if (character.equals("ڈ Ddal"))
        {
            i = 12;
        }
        else if (character.equals("ذ Zaal"))
        {
            i = 13;
        }
        else if (character.equals("ر Ray"))
        {
            i = 14;
        }
        else if (character.equals("ڑ Rray"))
        {
            i = 15;
        }
        else if (character.equals("ز Zay"))
        {
            i = 16;
        }
        else if (character.equals("ژ Ssay"))
        {
            i = 17;
        }
        else if (character.equals("س Seen"))
        {
            i = 18;
        }
        else if (character.equals("ش Sheen"))
        {
            i = 19;
        }
        else if (character.equals("ص Suaad"))
        {
            i = 20;
        }
        else if (character.equals("ض Zuaad"))
        {
            i = 21;
        }
        else if (character.equals("ط Toayein"))
        {
            i = 22;
        }
        else if (character.equals("ظ Zoayein"))
        {
            i = 23;
        }
        else if (character.equals("ع Aayein"))
        {
            i = 24;
        }
        else if (character.equals("غ Ghayein"))
        {
            i = 25;
        }
        else if (character.equals("ف Fay"))
        {
            i = 26;
        }
        else if (character.equals("ق Qaaf"))
        {
            i = 27;
        }
        else if (character.equals("ک Kaaf"))
        {
            i = 28;
        }
        else if (character.equals("گ Gaaf"))
        {
            i = 29;
        }
        else if (character.equals("ل Laam"))
        {
            i = 30;
        }
        else if (character.equals("م Meem"))
        {
            i = 31;
        }
        else if (character.equals("ن Noon"))
        {
            i = 32;
        }
        else if (character.equals("و Wao"))
        {
            i = 33;
        }
        else if (character.equals("ہ Choti haa"))
        {
            i = 34;
        }
        else if (character.equals("ء Hamza"))
        {
            i = 35;
        }
        else if (character.equals("ی Choti yay"))
        {
            i = 36;
        }
        else if (character.equals("ے Barri yay"))
        {
            i = 37;
        }
        return i;
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
