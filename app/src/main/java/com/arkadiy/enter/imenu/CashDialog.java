package com.arkadiy.enter.imenu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * Created by vadnu on 01/02/2018.
 */

public class CashDialog extends DialogFragment implements View.OnClickListener {
    View v;
    ImageButton one;
    ImageButton two;
    ImageButton three;
    ImageButton four;
    ImageButton five;
    ImageButton six;
    ImageButton seven;
    ImageButton eight;
    ImageButton nine;
    ImageButton enter;
    ImageButton zero;
    ImageButton z;
private Context context;
    ImageButton twoHundred;
    ImageButton oneHundred;
    ImageButton fifty;
    ImageButton twenty;
    ImageButton ten;
    ImageButton fiveShekel;
    ImageButton twoShekel;
    ImageButton oneShekel;
    ImageButton halfShekel;
    ImageButton tenAgorot;
    private String t;
    private Format format;

    private EditText editTextTotal;
    private String payed="0";
    private float pay=0;
    private float pay2=0;

private CalculateChangeListener listener;


    public View onCreateView(LayoutInflater inflator, ViewGroup container, Bundle savedInstance){

        v=inflator.inflate(R.layout.fragment_cash,container);

        try{
            one=(ImageButton)v.findViewById(R.id.button1);
            two=(ImageButton)v.findViewById(R.id.button2);
            three=(ImageButton)v.findViewById(R.id.button3);
            four=(ImageButton)v.findViewById(R.id.button4);
            five=(ImageButton)v.findViewById(R.id.button5);
            six=(ImageButton)v.findViewById(R.id.button6);
            seven=(ImageButton)v.findViewById(R.id.button7);
            eight=(ImageButton)v.findViewById(R.id.button8);
            nine=(ImageButton)v.findViewById(R.id.button9);
            zero=(ImageButton)v.findViewById(R.id.button0);
            enter=(ImageButton)v.findViewById(R.id.buttonEnter);
            z=(ImageButton)v.findViewById(R.id.buttonC);

            twoHundred=(ImageButton)v.findViewById(R.id.imageButtonTwoHundred);
            oneHundred=(ImageButton)v.findViewById(R.id.imageButtonOneHundred);
            fifty=(ImageButton)v.findViewById(R.id.imageButtonFifty);
            twenty=(ImageButton)v.findViewById(R.id.imageButtonTwenty);
            ten=(ImageButton)v.findViewById(R.id.imageButtonTen);
            fiveShekel=(ImageButton)v.findViewById(R.id.imageButtonFive);
            twoShekel=(ImageButton)v.findViewById(R.id.imageButtonTwo);
            oneShekel=(ImageButton)v.findViewById(R.id.imageButtonOne);
            halfShekel=(ImageButton)v.findViewById(R.id.imageButtonHalf);
            tenAgorot=(ImageButton)v.findViewById(R.id.imageButtonTenAgorot);
            format=new DecimalFormat("##.00");

        }catch(Exception ex){
            ex.printStackTrace();
        }


        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        six.setOnClickListener(this);
        seven.setOnClickListener(this);
        eight.setOnClickListener(this);
        nine.setOnClickListener(this);
        zero.setOnClickListener(this);
        enter.setOnClickListener(this);
        z.setOnClickListener(this);
        twoHundred.setOnClickListener(this);
        oneHundred.setOnClickListener(this);
        fifty.setOnClickListener(this);
        twenty.setOnClickListener(this);
        ten.setOnClickListener(this);
        fiveShekel.setOnClickListener(this);
        twoShekel.setOnClickListener(this);
        oneShekel.setOnClickListener(this);
        halfShekel.setOnClickListener(this);
        tenAgorot.setOnClickListener(this);


        this.setTotalEditText();

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CalculateChangeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement EditNameDialogListener");
        }
    }



    public View getView(){
        return v;
    }

    public void setTotalEditText(){
        editTextTotal=(EditText)v.findViewById(R.id.textViewTotal);
        t=this.getArguments().getString("total");
        editTextTotal.setText(t);
    }

    @Override
    public void onResume(){
        super.onResume();



    }

    public void calc_onClick(View view){

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button0:

                break;
            case R.id.button1:
                pay=1;
                break;
            case R.id.button2:
                pay=2;
                break;
            case R.id.button3:
                pay=3;
                break;
            case R.id.button4:
                pay=4;
                break;
            case R.id.button5:
                pay=5;
                break;
            case R.id.button6:
                pay=6;
                break;
            case R.id.button7:
                pay=7;
                break;
            case R.id.button8:
                pay=8;
                break;
            case R.id.button9:
                pay=9;
                break;
            case R.id.imageButtonTwoHundred:
                pay=200;
                break;
            case R.id.imageButtonOneHundred:
                pay=100;
                break;
            case R.id.imageButtonFifty:
                pay=50;
                break;
            case R.id.imageButtonTwenty:
                pay=20;
                break;
            case R.id.imageButtonTen:
                pay=10;
                break;
            case R.id.imageButtonFive:
                pay=5;
                break;
            case R.id.imageButtonTwo:
                pay=2;
                break;
            case R.id.imageButtonOne:
                pay=1;
                break;
            case R.id.imageButtonHalf:
                pay= (float) 0.5;
                break;
            case R.id.imageButtonTenAgorot:
                pay=(float)0.1;
                break;
            case R.id.buttonEnter:

listener.Calculate(Float.parseFloat(t),pay2);
this.dismiss();
return;

        }


        Float p=Float.parseFloat(t);
        p-=pay;
        pay2+=pay;
        t=p.toString();

        editTextTotal.setText(format.format(p));


    }


public String getPayed(){
        return t;
}





}
