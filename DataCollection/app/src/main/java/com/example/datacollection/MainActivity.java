package com.example.datacollection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    MyDbHandler db;
    EditText Name;
    EditText Age;
    EditText Phone;
    EditText BaseCity;
    Spinner Qualification;
    EditText CurrLoc;
    private RadioGroup radioSexGroup;
    private RadioButton radioSexButton;
    private RadioGroup radioWhandednessGroup;
    private RadioButton radioWhandednessButton;
    private RadioGroup radioDhandednessGroup;
    private RadioButton radioDhandednessButton;
    int selectedId;
    long userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //int width  = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        db = new MyDbHandler(this, "HandwritingData.db", null, 1);

        Name = (EditText)findViewById(R.id.Nametxt);
        Age = (EditText)findViewById(R.id.Agetxt);
        Phone = (EditText)findViewById(R.id.Phonetxt);
        BaseCity = (EditText)findViewById(R.id.Citytxt);
        CurrLoc = (EditText)findViewById(R.id.Locationtxt);
        radioSexGroup = (RadioGroup) findViewById(R.id.radioSex);
        radioWhandednessGroup = (RadioGroup)findViewById(R.id.Whandedness);
        radioDhandednessGroup = (RadioGroup)findViewById(R.id.Dhandedness);
        Qualification = (Spinner)findViewById(R.id.Qualificationtxt);


        Bundle bundle = getIntent().getExtras();
        //stuff = bundle.getString("stuff");


        List<String> objects = new ArrayList<>();
        objects.add(0, "--Qualification level--");
        objects.add("Under Matric");
        objects.add("Matric");
        objects.add("Intermediate");
        objects.add("Bachelors");
        objects.add("Masters");
        objects.add("Doctoral");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, objects);
        adapter.setDropDownViewResource(R.layout.spinner_item);

        Qualification.setAdapter(adapter);
    }

    public void Next(View view)
    {
        String text = Qualification.getSelectedItem().toString();

        if (Age.getText().toString().isEmpty() || BaseCity.getText().toString().isEmpty() || Phone.getText().toString().isEmpty() ||
        text.isEmpty() || CurrLoc.getText().toString().isEmpty())
        {
            Toast toast = Toast.makeText(getApplicationContext(), "All fields are required!", Toast.LENGTH_SHORT);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
        }

        else if (text == "--Qualification level--")
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select your Qualification level!", Toast.LENGTH_SHORT);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(20);
            toast.show();
        }
        else {
            //save data in database
            selectedId = radioSexGroup.getCheckedRadioButtonId();
            radioSexButton = (RadioButton) findViewById(selectedId);

            selectedId = radioWhandednessGroup.getCheckedRadioButtonId();
            radioWhandednessButton = (RadioButton) findViewById(selectedId);

            selectedId = radioDhandednessGroup.getCheckedRadioButtonId();
            radioDhandednessButton = (RadioButton) findViewById(selectedId);


            userid = db.addUserData(Name.getText().toString(), Age.getText().toString(), radioSexButton.getText().toString(),
                        BaseCity.getText().toString(), Phone.getText().toString(), radioWhandednessButton.getText().toString(),
                        radioDhandednessButton.getText().toString(), Qualification.getSelectedItem().toString(), CurrLoc.getText().toString());


            //go to next activity
            Intent intent = new Intent(MainActivity.this, UnguidedWritingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("stuff", Long.toString(userid));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

}
