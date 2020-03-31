package com.example.datacollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class WritingView extends AppCompatActivity {

    private CanvasView canvasView;
    MyDbHandler db;
    String character;
    int w;
    int h;
    String stuff;
    int i = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_view);
        canvasView = (CanvasView)findViewById(R.id.canvas);
        canvasView.setBackgroundResource(R.drawable.alif);
        canvasView.setTag(R.drawable.alif);
        character = "ا";
        //width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        //height = Resources.getSystem().getDisplayMetrics().heightPixels;

        db = new MyDbHandler(this, "HandwritingData.db", null, 1);
    }

    public void Next(View view)
    {
        character = getCharacter(i);
        h = canvasView.getHeight();
        w = canvasView.getWidth();
        Bundle bundle = getIntent().getExtras();
        stuff = bundle.getString("stuff");
        if (canvasView.listX.size() != 0) {
            String id = db.getStrokeid(Integer.parseInt(stuff),getCharacter(i),"Dotted Characters");
            if (id == null) {
                db.addStrokeData(canvasView, Integer.parseInt(stuff), w, h, getCharacter(i), "Dotted Characters", getDeviceName());
            }
            else
            {
                db.updateStrokeData(canvasView, Integer.parseInt(db.getStrokeid(Integer.parseInt(stuff),getCharacter(i),"Dotted Characters")));
            }
        }
        canvasView.clearCanvas();


        if(i == 37) {
            Intent intent = new Intent(WritingView.this, End.class);
            startActivity(intent);
        }
        i++;
        setBackground(i);
    }

    public void Back(View view)
    {
        canvasView.clearCanvas();
        if ( i > 1)
        {
            i--;
            setBackground(i);
        }
    }

    public void setBackground(int i)
    {
        if (i == 1)
        {
            canvasView.setBackgroundResource(R.drawable.alif);
            canvasView.setTag(R.drawable.alif);
            character = "ا";
        }
        else if (i == 2)
        {
            canvasView.setBackgroundResource(R.drawable.bay);
            canvasView.setTag(R.drawable.bay);
            character = "ب";
        }
        else if (i == 3)
        {
            canvasView.setBackgroundResource(R.drawable.pay);
            canvasView.setTag(R.drawable.pay);
            character = "پ";
        }
        else if (i == 4)
        {
            canvasView.setBackgroundResource(R.drawable.tay);
            canvasView.setTag(R.drawable.tay);
            character = "ت";
        }
        else if (i == 5)
        {
            canvasView.setBackgroundResource(R.drawable.ttay);
            canvasView.setTag(R.drawable.ttay);
            character = "ٹ";
        }
        else if (i == 6)
        {
            canvasView.setBackgroundResource(R.drawable.say);
            canvasView.setTag(R.drawable.say);
            character = "ث";
        }
        else if (i == 7)
        {
            canvasView.setBackgroundResource(R.drawable.jeem);
            canvasView.setTag(R.drawable.jeem);
            character = "ج";
        }
        else if (i == 8)
        {
            canvasView.setBackgroundResource(R.drawable.chay);
            canvasView.setTag(R.drawable.chay);
            character = "چ";
        }
        else if (i == 9)
        {
            canvasView.setBackgroundResource(R.drawable.hay);
            canvasView.setTag(R.drawable.hay);
            character = "ح";
        }
        else if (i == 10)
        {
            canvasView.setBackgroundResource(R.drawable.khay);
            canvasView.setTag(R.drawable.khay);
            character = "خ";
        }
        else if (i == 11)
        {
            canvasView.setBackgroundResource(R.drawable.daal);
            canvasView.setTag(R.drawable.daal);
            character = "د";
        }
        else if (i == 12)
        {
            canvasView.setBackgroundResource(R.drawable.ddal);
            canvasView.setTag(R.drawable.ddal);
            character = "ڈ";
        }
        else if (i == 13)
        {
            canvasView.setBackgroundResource(R.drawable.zaal);
            canvasView.setTag(R.drawable.zaal);
            character = "ذ";
        }
        else if (i == 14)
        {
            canvasView.setBackgroundResource(R.drawable.ray);
            canvasView.setTag(R.drawable.ray);
            character = "ر";
        }
        else if (i == 15)
        {
            canvasView.setBackgroundResource(R.drawable.rray);
            canvasView.setTag(R.drawable.rray);
            character = "ڑ";
        }
        else if (i == 16)
        {
            canvasView.setBackgroundResource(R.drawable.zay);
            canvasView.setTag(R.drawable.zay);
            character = "ز";
        }
        else if (i == 17)
        {
            canvasView.setBackgroundResource(R.drawable.ssay);
            canvasView.setTag(R.drawable.ssay);
            character = "ژ";
        }
        else if (i == 18)
        {
            canvasView.setBackgroundResource(R.drawable.seen);
            canvasView.setTag(R.drawable.seen);
            character = "س";
        }
        else if (i == 19)
        {
            canvasView.setBackgroundResource(R.drawable.sheen);
            canvasView.setTag(R.drawable.sheen);
            character = "ش";
        }
        else if (i == 20)
        {
            canvasView.setBackgroundResource(R.drawable.suaad);
            canvasView.setTag(R.drawable.suaad);
            character = "ص";
        }
        else if (i == 21)
        {
            canvasView.setBackgroundResource(R.drawable.zuaad);
            canvasView.setTag(R.drawable.zuaad);
            character = "ض";
        }
        else if (i == 22)
        {
            canvasView.setBackgroundResource(R.drawable.toayein);
            canvasView.setTag(R.drawable.toayein);
            character = "ط";
        }
        else if (i == 23)
        {
            canvasView.setBackgroundResource(R.drawable.zoayein);
            canvasView.setTag(R.drawable.zoayein);
            character = "ظ";
        }
        else if (i == 24)
        {
            canvasView.setBackgroundResource(R.drawable.aayein);
            canvasView.setTag(R.drawable.aayein);
            character = "ع";
        }
        else if (i == 25)
        {
            canvasView.setBackgroundResource(R.drawable.ghayein);
            canvasView.setTag(R.drawable.ghayein);
            character = "غ";
        }
        else if (i == 26)
        {
            canvasView.setBackgroundResource(R.drawable.fay);
            canvasView.setTag(R.drawable.fay);
            character = "ف";
        }
        else if (i == 27)
        {
            canvasView.setBackgroundResource(R.drawable.qaaf);
            canvasView.setTag(R.drawable.qaaf);
            character = "ق";
        }
        else if (i == 28)
        {
            canvasView.setBackgroundResource(R.drawable.kaaf);
            canvasView.setTag(R.drawable.kaaf);
            character = "ک";
        }
        else if (i == 29)
        {
            canvasView.setBackgroundResource(R.drawable.gaaf);
            canvasView.setTag(R.drawable.gaaf);
            character = "گ";
        }
        else if (i == 30)
        {
            canvasView.setBackgroundResource(R.drawable.laam);
            canvasView.setTag(R.drawable.laam);
            character = "ل";
        }
        else if (i == 31)
        {
            canvasView.setBackgroundResource(R.drawable.meem);
            canvasView.setTag(R.drawable.meem);
            character = "م";
        }
        else if (i == 32)
        {
            canvasView.setBackgroundResource(R.drawable.noon);
            canvasView.setTag(R.drawable.noon);
            character = "ن";
        }
        else if (i == 33)
        {
            canvasView.setBackgroundResource(R.drawable.wao);
            canvasView.setTag(R.drawable.wao);
            character = "و";
        }
        else if (i == 34)
        {
            canvasView.setBackgroundResource(R.drawable.haa);
            canvasView.setTag(R.drawable.haa);
            character = "ہ";
        }
        else if (i == 35)
        {
            canvasView.setBackgroundResource(R.drawable.hamza);
            canvasView.setTag(R.drawable.hamza);
            character = "ء";
        }
        else if (i == 36)
        {
            canvasView.setBackgroundResource(R.drawable.chotiyay);
            canvasView.setTag(R.drawable.chotiyay);
            character = "ی";
        }
        else if (i == 37)
        {
            canvasView.setBackgroundResource(R.drawable.bariyay);
            canvasView.setTag(R.drawable.bariyay);
            character = "ے";
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
