package com.example.urduhandwritingtutor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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
    private TensorFlowInferenceInterface inferenceInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        canvasView = (CanvasView)findViewById(R.id.canvas);
        db = new MyDbHandler(this, "UrduHandwritingTutor.db", null, 1);
        bundle = getIntent().getExtras();
        ComingFrom = bundle.getString("ComingFrom");
        nextBtn = findViewById(R.id.evaluate);

        if (ComingFrom == "PracticeActivity")
        {
            stuff = bundle.getString("character");
            charText = findViewById(R.id.chartext);
            charText.setText("Test: " + stuff);
        }
        else
        {
            evaluation_class evaluations = db.getEvaluations();
            HashSet<String> charactersSet = new HashSet<>(evaluations.Characters);
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
                num = 2;
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
        float[] floatArray = PreprocessData(canvasView);
        float[] output = predict(floatArray);

        List<Float> result = new ArrayList<>();
        for (float f : output) {
            result.add(f);
        }

        //float confidence = Collections.max(result);

        int i = bundle.getInt("Number");
        float confidence = result.get(i);

        String Feedback = "Number of Strokes: Correct\n\nOrder of Strokes: correct\n\nStroke Direction: Correct";
        db.addevaluation(stuff, Math.round(confidence*100), Feedback);
        bundle.putInt("Score", Math.round(confidence*100));
        bundle.putString("ComingFrom", "TestActivity");
        Intent intent = new Intent(TestActivity.this, FeedbackActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        TestActivity.this.finish();

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

        while (Resampled_X.size() < count && i + j < X.size() - 1)
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

    public List<Float> normalizeLIst(List<Float> list)
    {
        List<Float> normalized_list = new ArrayList<>();
        for (int i = 0; i < list.size(); i++)
        {
            normalized_list.add((((float)0.8)*(list.get(i) - Collections.min(list))/(Collections.max(list) - Collections.min(list)))+ (float) 0.1);
        }
        return normalized_list;
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

    float[] PreprocessData(CanvasView cv)
    {
        List<Float> listX = new ArrayList<>();
        List<Float> listY = new ArrayList<>();
        List<Long> listT = new ArrayList<>();
        //db.addStrokeData(cv);
        for (Float f : cv.listX)
        {
            listX.add(f);
        }
        for (Float f : cv.listY)
        {
            listY.add(f);
        }
        for (Long f : cv.TimeList)
        {
            listT.add(f);
        }

        //listX = cv.listX;
        //listY = cv.listY;
        //listT = cv.TimeList;
        cv.clearCanvas();
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

            if (listX_Dot.size() > listX_withoutDot.size())
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

            if (listX_Dot.size() > 6)
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

        int h = cv.getHeight();
        int w = cv.getWidth();

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
}
