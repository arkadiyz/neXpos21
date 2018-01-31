package com.arkadiy.enter.imenu;



import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by Arkadiy on 1/28/2018.
 */

public class PrintReceipt  {

    private final String title = "אנליזה";

    private String name = "שם";
    private String amount = "כמות";
    private String price = "מחיר";
    private String address = "גט רימון";
    private String address2 = "האילנות ";
    private String numberAddress = "19";
    private String hP = "03.32244242";
    private String totalPrice="";
    private String totalStringName="שולם ";
    private String coinString = " ש'ח ";
    private final int STANDART_LENGTH = 48;
    private String string = "";
    private ArrayList<Product> product;
    private PrinterManager printerManager;
    private char[]boldStart = {0x1B,0x45,0x31};
    private char[]boldFinish = {0x1B,0x45,0x30};
    private char[]cutChar={0x1D,0x56,0x41,0x10};
    private char[] setFontSize={0x1B,0x21};
    private char[] removeFontSize={0x1B,0x21,0x00};
    private char[] song = {7};
    private String stringFontSize="";
    private String cutString;
    private String boldStringStart;
    private String boldStringEnd;
    private int numberSize;

    public PrintReceipt(ArrayList<Product> product, Context ctx,String total)
    {
        this.printerManager = PrinterManager_.getInstance_(ctx);
        this.product = product;
        this.numberSize = 0;
        this.totalPrice = total;
        setBold();
        string = hebrewString(title);
        this.string = centerLine(string);
        this.string = endOfLine(this.string);
        printerManager.printJob(new PrinterJob(this.string));
        removeBold();


        this.address = hebrewString(this.address);
        this.address = endOfLine(this.address);
        this.printerManager.printJob(new PrinterJob(this.address));
        this.address2 = hebrewString(this.address2);
        this.hP = hebrewString(this.hP);
        this.printerManager.printJob(new PrinterJob(endOfLine(printCompanyInformation())));
        this.printerManager.printJob(new PrinterJob(endOfLine("")));
        // start print product
        this.name = hebrewString(this.name);
        this.amount = hebrewString(this.amount);
        this.price = hebrewString(this.price);
        arrangeARow(this.name,this.amount,this.price);

        this.printerManager.printJob(new PrinterJob(endOfLine(this.price+this.amount+this.name)));


        this.product = product;
        for ( Product p : this.product) {
            this.name=hebrewString(p.getProductName());
            arrangeARow(this.name,p.getAmount(),p.getPrice());
            printerManager.printJob(new PrinterJob(endOfLine(this.price+this.amount+this.name)));
        }

        SetDoubleStrike();
        // ~~~~~~~~~~~~~~print total price~~~~~~~~~~~~~~~~~~~~~
        this.totalStringName = hebrewString(this.totalStringName);
        this.coinString = hebrewString(this.coinString);
        this.totalPrice = centerLine(this.coinString+this.totalPrice+this.totalStringName);
        printerManager.printJob(new PrinterJob(this.totalPrice));
        removeDoubleStrike();

        this.printerManager.printJob(new PrinterJob("\n"));

        cutFunction();

    }



    private void song(){
        String str = new String(song);
        printerManager.printJob(new PrinterJob(str));
    }
    private void arrangeARow(String name,String amount,String price){
        int num1  = name.length();
        int num2  = 0;
        String str="";
        if (num1 < 25)
        {
            num2 = 25-num1;
            num1=num2;
        }
        if (num1 +name.length() == 25)
        {
            this.name="";
            for (int i = 0 ; i < num1 ; i++)
            {
                this.name+="_";
            }
            this.name+=name;
        }
        num1 = amount.length();
        if(num1 < 4)
        {
            num1+=15;
        }
        else num1+=10;
        this.amount="";
        for(int i = 0 ;i <num1;i++)
        {
            this.amount+="_";
        }
        this.amount+=amount;
        num1=price.length();

        if(num1 > 3) num1-=1;
        else num1+=4;
        this.price="";
        for (int i = 0;i < num1;i++)
        {
            this.price="_";
        }
        this.price+=price;

    }
    private void SetDoubleStrike()
    {
        numberSize = 1;
        this.stringFontSize = new String(this.setFontSize);
        printerManager.printJob(new PrinterJob(stringFontSize+numberSize));
    }
    private void removeDoubleStrike(){
        numberSize=0;
        stringFontSize = new String(removeFontSize);
        printerManager.printJob(new PrinterJob(stringFontSize));
    }
    private String printCompanyInformation(){
        int sumString1 = this.address2.length();
        int sumString2 = this.hP.length();
        int sumString3 = this.numberAddress.length();
        int sum = sumString1+ sumString2 + sumString3;
        sum = STANDART_LENGTH - sum;
        String str="";
        str +=this.hP;
        for(int i = 0 ; i < sum-1 ; i++)
        {
            str +="_";
        }
        str +=this.numberAddress + this.address2;


        return str;
    }
    private String endOfLine(String str){

        String str2="";
        if(str.length() != STANDART_LENGTH)
        {
            int num = (STANDART_LENGTH-str.length());
            if(numberSize>0)
            {
                num = num/4;
            }
            for(int i = 0 ; i < num;i++)
            {
                str2 +="!";
            }

        }
        str2+=str;
        return str2;
    }

    private void cutFunction(){
        cutString = new String(cutChar);
        printerManager.printJob(new PrinterJob(cutString));
    }
    private String  hebrewString(String s ){
        String str2="";
        char []charArray = new char [s.length()];
        char []charArray2 = new char [s.length()];
        for (int i =0 ; i<s.length();i++)
        {
            charArray[i] = s.charAt(i);
        }
        char ch;
        for (int i = s.length()-1,j=0; i>=0; i--,j++)
        {
            ch = charArray[j];
            if(checkingLetters(ch))
            {
                charArray2[i] = ch;
            }
            else
            {
                charArray2[j]= ch;
            }

        }
        for(int i = 0 ; i < s.length();i++)
        {
            str2 +=charArray2[i];
        }
        return str2;
    }
    private boolean checkingLetters(char ch){
        if(ch >= 1488 && ch <= 1514 || ch == 32)
            return true;
        return false;
    }

    private String centerLine (String str){
        int sumSpace = str.length()/2;
        int sumCharInLine = STANDART_LENGTH /2;
        sumSpace = (sumCharInLine - sumSpace);
        if(numberSize>0)
        {
            sumSpace=sumSpace/2;
        }
        String str2 = "";
        for(int i =0 ; i < sumSpace+1 ; i++)
        {
            str2 +="+";
        }
        str2+= str;
        int num = (STANDART_LENGTH-str2.length());
        if (numberSize>0)
        {
            num=num/2;
        }
        if(str2.length() != STANDART_LENGTH)
        {

            for(int i = 0 ; i < num;i++)
            {
                str2 +="!";
            }

        }
        return str2;


    }
    private void setBold(){
        boldStringStart=new String(boldStart);

        printerManager.printJob(new PrinterJob(boldStringStart));
    }
    private void removeBold(){
        boldStringEnd=new String(boldFinish);
        printerManager.printJob(new PrinterJob(boldStringEnd));
    }
}
