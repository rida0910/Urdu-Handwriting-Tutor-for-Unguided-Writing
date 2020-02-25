package com.example.urduhandwritingtutor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.ViewGroup;

public class RegisterActivity extends AppCompatActivity {

    TextView LoginTxt;
    EditText Name, Age, EmailAddress, Password;
    private RadioGroup radioWhandednessGroup;
    private RadioButton radioWhandednessButton;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    int selectedId;
    long userid;
    MyDbHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        db = new MyDbHandler(this, "UrduHandwritingTutor.db", null, 1);

        LoginTxt = (TextView)findViewById(R.id.login);
        Name = (EditText)findViewById(R.id.Name);
        Age = (EditText)findViewById(R.id.Age);
        EmailAddress = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioWhandednessGroup = (RadioGroup)findViewById(R.id.Whandedness);

        LoginTxt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                RegisterActivity.this.finish();
            }
        });
    }
    public void register(View view)
    {
        if (Age.getText().toString().isEmpty() || Name.getText().toString().isEmpty() || EmailAddress.getText().toString().isEmpty() ||
                Password.getText().toString().isEmpty())
        {
            Toast toast = Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
        }
        else
        {
            selectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton = (RadioButton) findViewById(selectedId);

            selectedId = radioWhandednessGroup.getCheckedRadioButtonId();
            radioWhandednessButton = (RadioButton) findViewById(selectedId);

            userid = db.addUserData(Name.getText().toString(), Age.getText().toString(), radioSexButton.getText().toString(),
                    EmailAddress.getText().toString(), Password.getText().toString(), radioWhandednessButton.getText().toString());


            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("id", Long.toString(userid));
            intent.putExtras(bundle);
            startActivity(intent);
            RegisterActivity.this.finish();
        }
    }
}
