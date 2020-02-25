package com.example.urduhandwritingtutor;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TestActivity extends AppCompatActivity {

    CanvasView canvasView;
    String stuff;
    Bundle bundle;
    MyDbHandler db;
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
        stuff = bundle.getString("character");
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
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(100) + 1;
        int i = bundle.getInt("Number");
        db.addevaluation(i, randomInt);

        Intent intent = new Intent(TestActivity.this, FeedbackActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        TestActivity.this.finish();
    }

    public void clearCanvas(View view)
    {
        canvasView.clearCanvas();
    }
}
