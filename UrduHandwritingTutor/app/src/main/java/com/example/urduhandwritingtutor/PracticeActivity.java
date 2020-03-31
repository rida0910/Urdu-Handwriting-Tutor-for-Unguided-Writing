package com.example.urduhandwritingtutor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.icu.util.ValueIterator;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {

    TextView charTxt;
    String stuff;
    CanvasView canvasView;
    Bundle bundle;
    ImageView img;
    Animatable animatable;
    int index;
    AnimatedVectorDrawableCompat an;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        charTxt = (TextView)findViewById(R.id.chartext);
        bundle = getIntent().getExtras();
        stuff = bundle.getString("character");
        i = bundle.getInt("Number");

        charTxt.setText(stuff);

        canvasView = (CanvasView)findViewById(R.id.canvas);
        //colorbtn = (ImageButton) findViewById(R.id.colorbtn);

        Typeface urdu = Typeface.createFromAsset(getAssets(), "Jameel Noori Nastaleeq.ttf");
        charTxt.setTypeface(urdu);

        getVector(i);
        canvasView.setImageDrawable(an);
        //an.start();

        an.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
            @Override
            public void onAnimationStart(Drawable drawable) {
                super.onAnimationStart(drawable);
            }
            @Override
            public void onAnimationEnd(Drawable drawable) {
                canvasView.post((new Runnable() {
                    @Override
                    public void run() {
                        an.start();
                    }
                }));
            }
        });

        an.start();



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

    public void start_test(View view)
    {
        Intent intent = new Intent(PracticeActivity.this, TestActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        PracticeActivity.this.finish();
    }

    public void clearCanvas(View view)
    {
        canvasView.clearCanvas();
    }

   public void play(View view) {

       //final AnimatedVectorDrawableCompat an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_bay);
       //img.setImageDrawable(an);
       getVector(i);
       canvasView.setImageDrawable(an);
       //an.start();

       an.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
           @Override
           public void onAnimationStart(Drawable drawable) {
               super.onAnimationStart(drawable);
           }
           @Override
           public void onAnimationEnd(Drawable drawable) {
               canvasView.post((new Runnable() {
                   @Override
                   public void run() {
                       an.start();
                   }
               }));
           }
       });

       an.start();
   }

   public void pause(View view)
   {
        an.clearAnimationCallbacks();
        //canvasView.setImageDrawable(null);
   }

   public void stop(View view)
   {
       canvasView.setImageDrawable(null);
   }

    public void getVector(int i)
    {
        if (i == 1)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_alif);
        }
        else if (i == 2)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_bay);
        }
        else if (i == 3)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_pay);
        }
        else if (i == 4)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_tay);
        }
        else if (i == 5)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_ttay);
        }
        else if (i == 6)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_say);
        }
        else if (i == 7)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_jeem);
        }
        else if (i == 8)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_chay);
        }
        else if (i == 9)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_hay);
        }
        else if (i == 10)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_khay);
        }
        else if (i == 11)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_daal);
        }
        else if (i == 12)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_ddaal);
        }
        else if (i == 13)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_zaal);
        }
        else if (i == 14)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_ray);
        }
        else if (i == 15)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_aray);
        }
        else if (i == 16)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_zay);
        }
        else if (i == 17)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_zyay);
        }
        else if (i == 18)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_seen);
        }
        else if (i == 19)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_sheen);
        }
        else if (i == 20)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_suad);
        }
        else if (i == 21)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_zuad);
        }
        else if (i == 22)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_tuen);
        }
        else if (i == 23)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_zuen);
        }
        else if (i == 24)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_ayin);
        }
        else if (i == 25)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_ghain);
        }
        else if (i == 26)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_fay);
        }
        else if (i == 27)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_qaaf);
        }
        else if (i == 28)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_kaaf);
        }
        else if (i == 29)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_gaaf);
        }
        else if (i == 30)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_laam);
        }
        else if (i == 31)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_meem);
        }
        else if (i == 32)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_noon);
        }
        else if (i == 33)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_wow);
        }
        else if (i == 34)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_hay_gol);
        }
        else if (i == 35)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_hamza);
        }
        else if (i == 36)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_chotiyay);
        }
        else if (i == 37)
        {
            an = AnimatedVectorDrawableCompat.create(this, R.drawable.avd_anim_bariyay);
        }
    }
}
