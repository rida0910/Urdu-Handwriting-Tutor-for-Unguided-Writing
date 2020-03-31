package com.example.datacollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PayActivity extends AppCompatActivity {

    private CanvasView canvasView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        canvasView = (CanvasView)findViewById(R.id.canvas);
    }

    public void Next(View view)
    {
        Intent intent = new Intent(PayActivity.this, TayActivity.class);
        startActivity(intent);
    }

    public void clearCanvas(View view) {
        canvasView.clearCanvas();
    }
}
