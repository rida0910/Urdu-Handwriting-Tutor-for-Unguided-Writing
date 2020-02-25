package com.example.urduhandwritingtutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView registerTxt;
    EditText EmailAddress, Password;
    Button login;
    MyDbHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        EmailAddress = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        login = (Button)findViewById(R.id.loginbtn);
        registerTxt = (TextView)findViewById(R.id.register);
        db = new MyDbHandler(this, "UrduHandwritingTutor.db", null, 1);
        registerTxt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });
    }
    public void Login(View view)
    {
        /*Context context = this;
        MediaPlayer mp;
        mp = MediaPlayer.create(context, R.raw.buttonclick);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });*/
        if (EmailAddress.getText().toString().isEmpty() || Password.getText().toString().isEmpty())
        {
            Toast toast = Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
        }
        else
        {
            String userid = db.LogInUser(EmailAddress.getText().toString(), Password.getText().toString());
            if (userid == null)
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Incorrect email or password!", Toast.LENGTH_SHORT);
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(20);
                toast.show();
            }
            else if (userid != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }
    }
}
