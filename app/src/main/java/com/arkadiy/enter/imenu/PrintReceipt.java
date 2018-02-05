package com.arkadiy.enter.imenu;



import android.content.Context;
import android.util.StringBuilderPrinter;

import java.util.ArrayList;

/**
 * Created by Arkadiy on 1/28/2018.
 */

public class PrintReceipt  {

    private final String title = "אנליזה" ;
    private String name = "שם";
    private String amount = "כמות";
    private String price = "מחיר";
    private String address = "גט רימון";
    private String address2 = "האילנות 19";
    private String numberAddress = "";
    private String hP = "03.32244242";
    private String totalPrice="";
    private String totalStringName="סה\"כ";
    private String coinString = "ש\"ח";
    private String StringOrderId = "";
    private String stringDateOpen = "נפתח ב ";
    private String stringDatePrint = "הודפס ב ";
    private String printData =  "";
    private String numberOrderId = "חשבונית מס/קבלה";
    private String totalTax = "סה\"כ מע\"ם";
    private final int STANDART_LENGTH = 48;
    private final int STANDART_LENGTH_BIG_SIZE = 24;
    private String string = "";
    private ArrayList<Product> product;
    private PrinterManager printerManager;
    private char[]boldStart = {0x1B,0x45,0x31};
    private char[]boldFinish = {0x1B,0x45,0x30};
    private char[]cutChar={0x1D,0x56,0x41,0x10};
    private char[] setFontSize={0x1B,0x21};
    private char[] removeFontSize={0x1B,0x21,0x00};
    private char[] openDrawer = {0x1B,0x70,0x00,0x32,0xfa};
    private String stringFontSize="";
    private String cutString;
    private String boldStringStart;
    private String boldStringEnd;
    private int numberSize;
    private Order order;
    private boolean englishL = false;

    public PrintReceipt(ArrayList<Product> product, Context ctx,String total,Order order,String data) {
        this.printerManager = PrinterManager_.getInstance_(ctx);
        this.product = product;
        this.numberSize = 0;
        this.totalPrice = total;
        this.order = order;
        this.printData = data;


        printTitel();
        printDate();
        // start print product
        // start print product
        startPrintProducts();

        // ~~~~~~~~~~~~~~print total price~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~print total price~~~~~~~~~~~~~~~~~~~~~
        printTotalPrice();

        //~~~~~~~~~~~~~~~~~~~~cut paper~~~~~~~~~~~~~~~~~~~~~~~~
        openDrawer();
        this.printerManager.printJob(new PrinterJob("\n"));
        cutFunction();

    }
    private void printDate(){
        String str = hebrewString(this.stringDateOpen);

        this.stringDateOpen = order.getDateAndTime();
        this.stringDateOpen += str;
        printerManager.printJob(new PrinterJob(endOfLine(this.stringDateOpen)));

        this.stringDatePrint = hebrewString(this.stringDatePrint);
        this.printData += this.stringDatePrint;
        printerManager.printJob(new PrinterJob(endOfLine(this.printData)));

    }
    private void printTotalPrice(){

        this.totalStringName = checkWord(this.totalStringName);
        this.coinString = checkWord(this.coinString);

        this.totalTax = checkWord(this.totalTax);

        this.totalTax = twoColumns(totalTax,"17%");
        printerManager.printJob(new PrinterJob(endOfLine(" ")));
        printerManager.printJob(new PrinterJob(this.totalTax));


        printerManager.printJob(new PrinterJob(this.totalTax));

        printerManager.printJob(new PrinterJob(this.totalTax));
        SetDoubleStrike();
        this.totalPrice = centerLine(this.coinString+this.totalPrice+this.totalStringName);
        printerManager.printJob(new PrinterJob(this.totalPrice));
        removeDoubleStrike();
    }
    private void startPrintProducts(){

        this.name = checkWord(this.name);
        this.amount = checkWord(this.amount);
        this.price = checkWord(this.price);

        arrangeARow(this.name,this.amount,this.price);

        this.printerManager.printJob(new PrinterJob(endOfLine(this.price+this.amount+this.name)));


        this.product = product;
        for ( Product p : this.product) {
            this.name=checkWord(p.getProductName());
            arrangeARow(this.name,p.getAmount(),p.getPrice());
            printerManager.printJob(new PrinterJob(endOfLine(this.price+this.amount+this.name)));
        }
    }
    private void printTitel() {
        setBold();
        this.string = checkWord(title);
        this.string = centerLine(string);
        this.string = endOfLine(this.string);
        printerManager.printJob(new PrinterJob(this.string));
        removeBold();
        openDrawer();

        this.address = checkWord(this.address);
        this.address = endOfLine(this.address);
        this.printerManager.printJob(new PrinterJob(this.address));
        this.address2 = checkWord(this.address2);
        this.hP = checkWord(this.hP);
        this.printerManager.printJob(new PrinterJob(endOfLine(twoColumns(this.address2,this.hP))));

        this.numberOrderId += String.valueOf(order.getIndex());
        this.numberOrderId = checkWord(this.numberOrderId);
        this.numberOrderId = centerLine(this.numberOrderId);
        this.printerManager.printJob(new PrinterJob(this.numberOrderId));
    }
    private void openDrawer(){
        String str = new String(openDrawer);
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
        if(num1 > 6)
        {
            num1 = num1-price.length()+6;

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
//        for (int i = 0;i < num1;i++)
//        {
//            this.price+="_";
//        }
        this.price+=price;

    }
    private void SetDoubleStrike() {
        numberSize = 1;
        this.stringFontSize = new String(this.setFontSize);
        printerManager.printJob(new PrinterJob(stringFontSize+numberSize));
    }
    private void removeDoubleStrike(){
        numberSize=0;
        stringFontSize = new String(removeFontSize);
        printerManager.printJob(new PrinterJob(stringFontSize));
    }
    private String twoColumns(String str1,String str2){
        int sumString1 = str1.length();
        int sumString2 = str2.length();
//        int sumString3 = str3.length();

        sumString1 = STANDART_LENGTH - sumString1 - sumString2;

        String str="";
        str = str2;
        for(int i = 0 ; i < sumString1 ; i++)
        {
            str +="_";
        }
        str +=str1;


        return str;
    }
    private String endOfLine(String str){

        String str2="";
        if(str.length() != STANDART_LENGTH)
        {
            int num = (STANDART_LENGTH-str.length());
            if(numberSize>0)
            {
                num = num/2;
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
    private String checkWord(String str){
        int numSpace = 0 ;
        String []str3;
        String str2="";
        String hebrewString="";

        boolean hebrewWordExists = false;
        str = str.trim();
        str3 = str.split(" ");
        if(str3.length == 1)
        {
            if(checkingLetters(str3[0].charAt(0)))
                str2 = hebrewString(str3[0]);
            else
                str2 = str3[0];
        }
        else
        {
            for(int i =0 ; i < str3.length ;i++)
            {

                if(!str3[i].isEmpty())
                {
                    if(checkingLetters(str3[i].charAt(0)))
                        str3[i] = hebrewString(str3[i]);
                }
                str2+=str3[i];


            }
                englishL = false;
                str2="";
                for(int i = str3.length -1; i >= 0 ; i--)
                {

                    str2 += str3[i];
                    str2+=" ";
                }

        }
        return str2;
    }
    private String hebrewString(String s){
        String str="";

          for(int i = s.length()-1 ; i >= 0;i--)
          {

              str += s.charAt(i);
          }
          return str;

      }
    private boolean checkingChars(char ch) {
        if( ch >=31 && ch <= 47 )
            return true;
        return false;
    }
    private boolean checkingLetters(char ch){
        if(ch >= 'א' && ch <= 'ת' )
            return true;
        englishL = true;
        return false;
    }
    private boolean checkingNumbers(char ch){
        if(ch >= 30 && ch <= 39) {
            return true;
        }
        return false;
    }
    private String centerLine (String str){
        int sumSpace = str.length()/2;
        int sumCharInLine = STANDART_LENGTH /2;
        sumSpace = (sumCharInLine - sumSpace);

        if(numberSize>0)
        {
            sumSpace = str.length()/2;
            sumCharInLine = STANDART_LENGTH_BIG_SIZE /2;
            sumSpace = (sumCharInLine - sumSpace);
        }
        String str2 = "";
        for(int i =0 ; i < sumSpace ; i++)
        {
            str2 +="+";
        }
        str2+= str;
         sumSpace = (STANDART_LENGTH-str2.length());
        if (numberSize>0)
        {
        sumSpace = str.length()/2;
        sumCharInLine = STANDART_LENGTH_BIG_SIZE /2;
        sumSpace = (sumCharInLine - sumSpace);

        }
        if(str2.length() != STANDART_LENGTH)
        {

            for(int i = 0 ; i < sumSpace;i++)
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
