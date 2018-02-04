package com.arkadiy.enter.imenu;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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

   private EditText editTextTotal;
    private String payed="0";
    private float pay=0;

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

    public View getView(){
        return v;
    }

    public void setTotalEditText(){
        editTextTotal=(EditText)v.findViewById(R.id.textViewTotal);
        String t=this.getArguments().getString("total");
        editTextTotal.setText(t);

    }

    public void calc_onClick(View view){

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.button0:

                break;
            case R.id.button1:
                pay=Float.parseFloat(payed);
                pay+=1;
                payed=Float.toString(pay);
                break;
            case R.id.button2:
                pay=Float.parseFloat(payed);
                pay+=2;
                payed=Float.toString(pay);
                break;
            case R.id.button3:
                pay=Float.parseFloat(payed);
                pay+=3;
                payed=Float.toString(pay);
                break;
            case R.id.button4:
                pay=Float.parseFloat(payed);
                pay+=4;
                payed=Float.toString(pay);
                break;
            case R.id.button5:
                pay=Float.parseFloat(payed);
                pay+=5;
                payed=Float.toString(pay);
                break;
            case R.id.button6:
                pay=Float.parseFloat(payed);
                pay+=6;
                payed=Float.toString(pay);
                break;
            case R.id.button7:
                pay=Float.parseFloat(payed);
                pay+=7;
                payed=Float.toString(pay);
                break;
            case R.id.button8:
                pay=Float.parseFloat(payed);
                pay+=8;
                payed=Float.toString(pay);
                break;
            case R.id.button9:
                pay=Float.parseFloat(payed);
                pay+=9;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonTwoHundred:
                pay=Float.parseFloat(payed);
                pay+=200;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonOneHundred:
                pay=Float.parseFloat(payed);
                pay+=100;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonFifty:
                pay=Float.parseFloat(payed);
                pay+=50;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonTwenty:
                pay=Float.parseFloat(payed);
                pay+=20;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonTen:
                pay=Float.parseFloat(payed);
                pay+=10;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonFive:
                pay=Float.parseFloat(payed);
                pay+=5;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonTwo:
                pay=Float.parseFloat(payed);
                pay+=2;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonOne:
                pay=Float.parseFloat(payed);
                pay+=1;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonHalf:
                pay=Float.parseFloat(payed);
                pay+=0.5;
                payed=Float.toString(pay);
                break;
            case R.id.imageButtonTenAgorot:
                pay=Float.parseFloat(payed);
                pay+=0.1;
                payed=Float.toString(pay);
                break;
            case R.id.buttonEnter:
                //update database and print reciept and delete from open orders
                break;

        }


        editTextTotal.setText(payed);


    }





}
