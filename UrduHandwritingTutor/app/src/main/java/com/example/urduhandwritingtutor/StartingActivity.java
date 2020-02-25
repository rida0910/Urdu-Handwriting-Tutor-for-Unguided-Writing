package com.example.urduhandwritingtutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class StartingActivity extends AppCompatActivity {

    MyDbHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting);
        getSupportActionBar().hide();
        db = new MyDbHandler(this, "UrduHandwritingTutor.db", null, 1);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String id = db.getLoggedInUser();
                if (id != null)
                {
                    final Intent mainIntent = new Intent(StartingActivity.this, MainActivity.class);
                    StartingActivity.this.startActivity(mainIntent);
                    StartingActivity.this.finish();
                }
                else if(id == null) {
                    final Intent mainIntent = new Intent(StartingActivity.this, LoginActivity.class);
                    StartingActivity.this.startActivity(mainIntent);
                    StartingActivity.this.finish();
                }
            }
        }, 3000);
    }
}
