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
    private TensorFlowInferenceInterface inferenceInterface;
    CountDownTimer countDownTimer;
    int counter = 30;
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
            evaluation_class evaluations = db.getEvaluations();
            HashSet<String> charactersSet = new HashSet<>(evaluations.Characters);
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

                QuizId = db.addQuiz();
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
        if (num == 5)
        {
            Intent intent = new Intent(TestActivity.this, MainActivity.class);
            startActivity(intent);
            TestActivity.this.finish();
        }

        if (canvasView.getListX().size() == 0)
        {
            Toast.makeText(this, "Please write the specified character!",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {

            //float confidence = Collections.max(result);

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
                //String Feedback = "Number of Strokes: Correct\n\nOrder of Strokes: correct\n\nStroke Direction: Correct";
                String Feedback = getFeedback(canvasView, i);
                db.addevaluation(stuff, Math.round(confidence * 100), Feedback);
                bundle.putInt("Score", Math.round(confidence * 100));
                bundle.putString("ComingFrom", "TestActivity");
                bundle.putString("Feedback", getFeedback(canvasView, i));
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
                //String Feedback = "Number of Strokes: Correct\n\nOrder of Strokes: correct\n\nStroke Direction: Correct";
                String Feedback = getFeedback(canvasView, getCharacterNumber(character));
                long evalID = db.addevaluation(character, Math.round(confidence*100), Feedback);
                db.addQuizResults(QuizId, evalID);
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

        List<Float> ResampledX = new ArrayList<>();
        List<Float> ResampledY = new ArrayList<>();
        XYCoord SmoothedXY = new XYCoord();
        if (dots_indices.size() != 0)
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

            List<Float> ResampledX_dot = new ArrayList<>();
            List<Float> ResampledY_dot = new ArrayList<>();
            XYCoord XYCoord;

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

        String numStrokes, OrderofStrokes, Direcction  = "Incorrect";
        boolean alpha = false;
        boolean beta = false;
        boolean gamma = false;

        //no. of strokes
        if (getNumofStrokes(canvasView.TimeList).size() == getCorrectnumofStrokes(character))
        {
            numStrokes = "Number of Strokes: " + getNumofStrokes(canvasView.TimeList).size() + " (Correct)";
            alpha = true;
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
        if (checkStrokeOrder(canvasView.getListX(), canvasView.getListY(), canvasView.TimeList, character) && alpha)
        {
            OrderofStrokes = "Order of Strokes: Correct";
            beta = true;
        }
        else
        {
            OrderofStrokes = "Base shape must be drawn first";
        }

        //now for direction of strokes\
        List<String> directions = getStrokeDirection(canvasView.getListX(), canvasView.getListY(), canvasView.TimeList);

        Feedback = numStrokes + "\n\n" + OrderofStrokes + "\n\nStroke Direction: Correct";

        return Feedback;
    }

    List<String> getStrokeDirection(List<Float> listX, List<Float> listY, List<Long> TimeList)
    {
        List<String> directions = new ArrayList<>();
        List<Integer> penDowns = getNumofStrokes(TimeList);

        Float deltaX, deltaY;
        double theta;

        /*if (penDowns.size() == 1)
        {*/
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
        //}

        return directions;
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
        else if (character.equals("ٹ Ttay"))
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

}
