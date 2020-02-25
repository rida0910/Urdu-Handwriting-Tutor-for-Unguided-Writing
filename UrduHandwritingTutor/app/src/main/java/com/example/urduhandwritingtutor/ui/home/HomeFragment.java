package com.example.urduhandwritingtutor.ui.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.urduhandwritingtutor.LoginActivity;
import com.example.urduhandwritingtutor.MainActivity;
import com.example.urduhandwritingtutor.PracticeActivity;
import com.example.urduhandwritingtutor.R;
import com.example.urduhandwritingtutor.RegisterActivity;
import com.example.urduhandwritingtutor.ui.evaluations;
import java.nio.BufferUnderflowException;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    Button alif;
    Button bay;
    Button pay;
    Button tay;
    Button ttay;
    Button say;
    Button jeem;
    Button chay;
    Button hay;
    Button khay;
    Button daal;
    Button ddal;
    Button zaal;
    Button ray;
    Button rray;
    Button zay;
    Button ssay;
    Button seen;
    Button sheen;
    Button suaad;
    Button zuaad;
    Button toayein;
    Button zoayein;
    Button aayein;
    Button ghayein;
    Button fay;
    Button qaaf;
    Button kaaf;
    Button gaaf;
    Button laam;
    Button meem;
    Button noon;
    Button wao;
    Button haa;
    Button hamza;
    Button choti_yay;
    Button barri_yay;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        alif = (Button)root.findViewById(R.id.alif);
        alif.setOnClickListener(this);
        bay = (Button)root.findViewById(R.id.bay);
        bay.setOnClickListener(this);
        pay = (Button)root.findViewById(R.id.pay);
        pay.setOnClickListener(this);
        tay = (Button)root.findViewById(R.id.tay);
        tay.setOnClickListener(this);
        ttay = (Button)root.findViewById(R.id.ttay);
        ttay.setOnClickListener(this);
        say = (Button)root.findViewById(R.id.say);
        say.setOnClickListener(this);
        jeem = (Button)root.findViewById(R.id.jeem);
        jeem.setOnClickListener(this);
        chay = (Button)root.findViewById(R.id.chay);
        chay.setOnClickListener(this);
        hay = (Button)root.findViewById(R.id.hay);
        hay.setOnClickListener(this);
        khay = (Button)root.findViewById(R.id.khay);
        khay.setOnClickListener(this);
        daal = (Button)root.findViewById(R.id.daal);
        daal.setOnClickListener(this);
        ddal = (Button)root.findViewById(R.id.ddaal);
        ddal.setOnClickListener(this);
        zaal = (Button)root.findViewById(R.id.zaal);
        zaal.setOnClickListener(this);
        ray = (Button)root.findViewById(R.id.ray);
        ray.setOnClickListener(this);
        rray = (Button)root.findViewById(R.id.rray);
        rray.setOnClickListener(this);
        zay = (Button)root.findViewById(R.id.zay);
        zay.setOnClickListener(this);
        Button ssay = (Button)root.findViewById(R.id.ssay);
        ssay.setOnClickListener(this);
        Button seen = (Button)root.findViewById(R.id.seen);
        seen.setOnClickListener(this);
        Button sheen = (Button)root.findViewById(R.id.sheen);
        sheen.setOnClickListener(this);
        Button suaad = (Button)root.findViewById(R.id.suaad);
        suaad.setOnClickListener(this);
        Button zuaad = (Button)root.findViewById(R.id.zuaad);
        zuaad.setOnClickListener(this);
        Button toayein = (Button)root.findViewById(R.id.toayein);
        toayein.setOnClickListener(this);
        Button zoayein = (Button)root.findViewById(R.id.zoayein);
        zoayein.setOnClickListener(this);
        Button aayein = (Button)root.findViewById(R.id.aayein);
        aayein.setOnClickListener(this);
        Button ghayein = (Button)root.findViewById(R.id.ghayein);
        ghayein.setOnClickListener(this);
        Button fay = (Button)root.findViewById(R.id.fay);
        fay.setOnClickListener(this);
        Button qaaf = (Button)root.findViewById(R.id.qaaf);
        qaaf.setOnClickListener(this);
        Button kaaf = (Button)root.findViewById(R.id.kaaf);
        kaaf.setOnClickListener(this);
        Button gaaf = (Button)root.findViewById(R.id.gaaf);
        gaaf.setOnClickListener(this);
        Button laam = (Button)root.findViewById(R.id.laam);
        laam.setOnClickListener(this);
        Button meem = (Button)root.findViewById(R.id.meem);
        meem.setOnClickListener(this);
        Button noon = (Button)root.findViewById(R.id.noon);
        noon.setOnClickListener(this);
        Button wao = (Button)root.findViewById(R.id.wao);
        wao.setOnClickListener(this);
        Button haa = (Button)root.findViewById(R.id.haa);
        haa.setOnClickListener(this);
        Button hamza = (Button)root.findViewById(R.id.hamza);
        hamza.setOnClickListener(this);
        Button choti_yay = (Button)root.findViewById(R.id.choti_yay);
        choti_yay.setOnClickListener(this);
        Button barri_yay = (Button)root.findViewById(R.id.barri_yay);
        barri_yay.setOnClickListener(this);


        Typeface urdu = Typeface.createFromAsset(getActivity().getAssets(), "Jameel Noori Nastaleeq.ttf");
        alif.setTypeface(urdu);
        bay.setTypeface(urdu);
        pay.setTypeface(urdu);
        tay.setTypeface(urdu);
        ttay.setTypeface(urdu);
        say.setTypeface(urdu);
        jeem.setTypeface(urdu);
        chay.setTypeface(urdu);
        hay.setTypeface(urdu);
        khay.setTypeface(urdu);
        daal.setTypeface(urdu);
        ddal.setTypeface(urdu);
        zaal.setTypeface(urdu);
        ray.setTypeface(urdu);
        rray.setTypeface(urdu);
        zay.setTypeface(urdu);
        ssay.setTypeface(urdu);
        seen.setTypeface(urdu);
        sheen.setTypeface(urdu);
        suaad.setTypeface(urdu);
        zuaad.setTypeface(urdu);
        toayein.setTypeface(urdu);
        zoayein.setTypeface(urdu);
        aayein.setTypeface(urdu);
        ghayein.setTypeface(urdu);
        fay.setTypeface(urdu);
        qaaf.setTypeface(urdu);
        kaaf.setTypeface(urdu);
        gaaf.setTypeface(urdu);
        laam.setTypeface(urdu);
        meem.setTypeface(urdu);
        noon.setTypeface(urdu);
        wao.setTypeface(urdu);
        haa.setTypeface(urdu);
        hamza.setTypeface(urdu);
        choti_yay.setTypeface(urdu);
        barri_yay.setTypeface(urdu);
        return root;
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();;
        Intent intent = new Intent(getActivity(), PracticeActivity.class);
        /*switch (v.getId()) {
            case R.id.alif:
                bundle.putString("character", "alif");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.bay:
                bundle.putString("character", "alif");
                intent.putExtras(bundle);
                startActivity(intent);
                break;*/

        if(v.getId() == R.id.alif)
        {
            bundle.putString("character", "ا Alif");
            bundle.putInt("Number", 1);
        }
        else if (v.getId() == R.id.bay)
        {
            bundle.putString("character", "ب Bay");
            bundle.putInt("Number", 2);
        }
        else if (v.getId() == R.id.pay)
        {
            bundle.putString("character", "پ Pay");
            bundle.putInt("Number", 3);
        }
        else if (v.getId() == R.id.tay)
        {
            bundle.putString("character", "ت Tay");
            bundle.putInt("Number", 4);
        }
        else if (v.getId() == R.id.ttay)
        {
            bundle.putString("character", "ٹ TTay");
            bundle.putInt("Number", 5);
        }
        else if (v.getId() == R.id.say)
        {
            bundle.putString("character", "ث Say");
            bundle.putInt("Number", 6);
        }
        else if (v.getId() == R.id.jeem)
        {
            bundle.putString("character", "ج Jeem");
            bundle.putInt("Number", 7);
        }
        else if (v.getId() == R.id.chay)
        {
            bundle.putString("character", "چ Chay");
            bundle.putInt("Number", 8);
        }
        else if (v.getId() == R.id.hay)
        {
            bundle.putString("character", "ح Hay");
            bundle.putInt("Number", 9);
        }
        else if (v.getId() == R.id.khay)
        {
            bundle.putString("character", "خ Khay");
            bundle.putInt("Number", 10);
        }
        else if (v.getId() == R.id.daal)
        {
            bundle.putString("character", "د Daal");
            bundle.putInt("Number", 11);
        }
        else if (v.getId() == R.id.ddaal)
        {
            bundle.putString("character", "ڈ DDal");
            bundle.putInt("Number", 12);
        }
        else if (v.getId() == R.id.zaal)
        {
            bundle.putString("character", "ذ Zaal");
            bundle.putInt("Number", 13);
        }
        else if (v.getId() == R.id.ray)
        {
            bundle.putString("character", "ر Ray");
            bundle.putInt("Number", 14);
        }
        else if (v.getId() == R.id.rray)
        {
            bundle.putString("character", "ڑ RRay");
            bundle.putInt("Number", 15);
        }
        else if (v.getId() == R.id.zay)
        {
            bundle.putString("character", "ز Zay");
            bundle.putInt("Number", 16);
        }
        else if (v.getId() == R.id.ssay)
        {
            bundle.putString("character", "ژ SSay");
            bundle.putInt("Number", 17);
        }
        else if (v.getId() == R.id.seen)
        {
            bundle.putString("character", "س Seen");
            bundle.putInt("Number", 18);
        }
        else if (v.getId() == R.id.sheen)
        {
            bundle.putString("character", "ش Sheen");
            bundle.putInt("Number", 19);
        }
        else if (v.getId() == R.id.suaad)
        {
            bundle.putString("character", "ص Suaad");
            bundle.putInt("Number", 20);
        }
        else if (v.getId() == R.id.zuaad)
        {
            bundle.putString("character", "ض Zuaad");
            bundle.putInt("Number", 21);
        }
        else if (v.getId() == R.id.toayein)
        {
            bundle.putString("character", "ط Toayein");
            bundle.putInt("Number", 22);
        }
        else if (v.getId() == R.id.zoayein)
        {
            bundle.putString("character", "ظ Zoayein");
            bundle.putInt("Number", 23);
        }
        else if (v.getId() == R.id.aayein)
        {
            bundle.putString("character", "ع Aayein");
            bundle.putInt("Number", 24);
        }
        else if (v.getId() == R.id.ghayein)
        {
            bundle.putString("character", "غ Ghayein");
            bundle.putInt("Number", 25);
        }
        else if (v.getId() == R.id.fay)
        {
            bundle.putString("character", "ف Fay");
            bundle.putInt("Number", 26);
        }
        else if (v.getId() == R.id.qaaf)
        {
            bundle.putString("character", "ق Qaaf");
            bundle.putInt("Number", 27);
        }
        else if (v.getId() == R.id.kaaf)
        {
            bundle.putString("character", "ک Kaaf");
            bundle.putInt("Number", 28);
        }
        else if (v.getId() == R.id.gaaf)
        {
            bundle.putString("character", "گ Gaaf");
            bundle.putInt("Number", 29);
        }
        else if (v.getId() == R.id.laam)
        {
            bundle.putString("character", "Laam ل");
            bundle.putInt("Number", 30);
        }
        else if (v.getId() == R.id.meem)
        {
            bundle.putString("character", "Meem م");
            bundle.putInt("Number", 31);
        }
        else if (v.getId() == R.id.noon)
        {
            bundle.putString("character", "Noon ن");
            bundle.putInt("Number", 32);
        }
        else if (v.getId() == R.id.wao)
        {
            bundle.putString("character", "Wao و");
            bundle.putInt("Number", 33);
        }
        else if (v.getId() == R.id.haa)
        {
            bundle.putString("character", "Haa ہ");
            bundle.putInt("Number", 34);
        }
        else if (v.getId() == R.id.hamza)
        {
            bundle.putString("character", "Hamza ء");
            bundle.putInt("Number", 35);
        }
        else if (v.getId() == R.id.choti_yay)
        {
            bundle.putString("character", "Choti yay ی");
            bundle.putInt("Number", 36);
        }
        else if (v.getId() == R.id.barri_yay)
        {
            bundle.putString("character", "Barri yay ے");
            bundle.putInt("Number", 37);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }
}