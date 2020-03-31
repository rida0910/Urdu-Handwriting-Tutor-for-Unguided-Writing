package com.example.datacollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class BaselineWritingActivity extends AppCompatActivity {

    private CanvasView canvasView;
    MyDbHandler db;
    int i = 1;
    String character;
    int width;
    int height;
    String stuff;
    TextView characterTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baseline_writing);
        canvasView = (CanvasView)findViewById(R.id.canvas);
        characterTextView = (TextView)findViewById(R.id.charTextView);

        //width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        //height = Resources.getSystem().getDisplayMetrics().heightPixels;

        db = new MyDbHandler(this, "HandwritingData.db", null, 1);
        characterTextView.setText("Write  " + getCharacter(i));
    }
    public void Next(View view)
    {
        int h = canvasView.getHeight();
        int w = canvasView.getWidth();

        width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        height = Resources.getSystem().getDisplayMetrics().heightPixels;

        Bundle bundle = getIntent().getExtras();
        stuff = bundle.getString("stuff");
        if (canvasView.listX.size() != 0) {
            String id = db.getStrokeid(Integer.parseInt(stuff),getCharacter(i),"With Baseline");
            if(id == null) {
                db.addStrokeData(canvasView, Integer.parseInt(stuff), w, h, getCharacter(i), "With Baseline", getDeviceName());
            }
            else
            {
                db.updateStrokeData(canvasView, Integer.parseInt(db.getStrokeid(Integer.parseInt(stuff),getCharacter(i),"With Baseline")));
            }
        }
        canvasView.clearCanvas();
        if (i == 37)
        {
            Intent intent = new Intent(BaselineWritingActivity.this, WritingView.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else {
            i++;
            characterTextView.setText("Write  " + getCharacter(i));
        }
    }

    public void Back(View view)
    {
        canvasView.clearCanvas();
        if ( i > 1)
        {
            i--;
            characterTextView.setText("Write  " + getCharacter(i));
        }
    }

    public String getCharacter(int i)
    {
        if (i == 1)
        {
            character = "ا";
        }
        else if (i == 2)
        {
            character = "ب";
        }
        else if (i == 3)
        {
            character = "پ";
        }
        else if (i == 4)
        {
            character = "ت";
        }
        else if (i == 5)
        {
            character = "ٹ";
        }
        else if (i == 6)
        {
            character = "ث";
        }
        else if (i == 7)
        {
            character = "ج";
        }
        else if (i == 8)
        {
            character = "چ";
        }
        else if (i == 9)
        {
            character = "ح";
        }
        else if (i == 10)
        {
            character = "خ";
        }
        else if (i == 11)
        {
            character = "د";
        }
        else if (i == 12)
        {
            character = "ڈ";
        }
        else if (i == 13)
        {
            character = "ذ";
        }
        else if (i == 14)
        {
            character = "ر";
        }
        else if (i == 15)
        {
            character = "ڑ";
        }
        else if (i == 16)
        {
            character = "ز";
        }
        else if (i == 17)
        {
            character = "ژ";
        }
        else if (i == 18)
        {
            character = "س";
        }
        else if (i == 19)
        {
            character = "ش";
        }
        else if (i == 20)
        {
            character = "ص";
        }
        else if (i == 21)
        {
            character = "ض";
        }
        else if (i == 22)
        {
            character = "ط";
        }
        else if (i == 23)
        {
            character = "ظ";
        }
        else if (i == 24)
        {
            character = "ع";
        }
        else if (i == 25)
        {
            character = "غ";
        }
        else if (i == 26)
        {
            character = "ف";
        }
        else if (i == 27)
        {
            character = "ق";
        }
        else if (i == 28)
        {
            character = "ک";
        }
        else if (i == 29)
        {
            character = "گ";
        }
        else if (i == 30)
        {
            character = "ل";
        }
        else if (i == 31)
        {
            character = "م";
        }
        else if (i == 32)
        {
            character = "ن";
        }
        else if (i == 33)
        {
            character = "و";
        }
        else if (i == 34)
        {
            character = "ہ";
        }
        else if (i == 35)
        {
            character = "ء";
        }
        else if (i == 36)
        {
            character = "ی";
        }
        else if (i == 37)
        {
            character = "ے";
        }
        return character;
    }

    public void clearCanvas(View view) {
        canvasView.clearCanvas();
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
