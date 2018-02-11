package com.arkadiy.enter.imenu;



import android.content.Context;
import android.util.StringBuilderPrinter;

import java.util.ArrayList;

/**
 * Created by Arkadiy on 1/28/2018.
 */

public class PrintReceipt  {

    private String companeName = "אנליזה" ;
    private String name = "שם";
    private String amount = "כמות";
    private String price = "מחיר";
    private String city = "";
    private String street = "האילנות 19";
    private String orderIdString = "מספר הזמנה-";
    private String hPString = "ח.פ-";
    private String totalPrice="";
    private String totalStringName="סה\"כ";
    private String coinString = "ש\"ח";
    private String stringDateOpen = "נפתח ב ";
    private String stringDatePrint = "הודפס ב ";
    private String printData =  "";
    private String stringTax = "חשבונית מס/קבלה-";
    private String totalTax = "אחוז מע\"ם";
    private String totalTaxbeForeString = "סה\"כ מע\"ם מגולם";
    private String sumBeForeTaxString = "סה\"כ לפני מע\"ם";
    private String cashPament = "מזומן";
    private String creditPament = "אשראי";
    private String acceptedString = "נתקבל:";
    private String changeString = "עודף";
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
    private CashInformation cashInformation;
    private String x;
    private String invoiceNum;
    private float tax;
    private float beforeTax;
    private String sourceCopyInvoice = "מקור";

    public PrintReceipt(Context ctx,Order order,String data,CashInformation cashInformation,String invoiceNum) {
        this.printerManager = PrinterManager_.getInstance_(ctx);

        this.order = order;
        this.product = order.getProducts();
        this.numberSize = 0;
        this.totalPrice = String.valueOf(order.getTotal());
        this.printData = data;
        this.cashInformation = cashInformation;
        this.invoiceNum=invoiceNum;
        this.tax=order.getMaam();
        this.beforeTax=order.getPriceBeforeTax();
        printTitel();
        printSourceCopyInvoice();
        printDate();
        this.printerManager.printJob(new PrinterJob(endOfLine(" ")));
        // start print product
        // start print product
        startPrintProducts();
        // ~~~~~~~~~~~~~~print total price~~~~~~~~~~~~~~~~~~~~~
        // ~~~~~~~~~~~~~~print total price~~~~~~~~~~~~~~~~~~~~~
        setLien();

        printTotalProducts();
        setLien();
        printTypePayment();
        printTotalPrice();

        //~~~~~~~~~~~~~~~~~~~~cut paper~~~~~~~~~~~~~~~~~~~~~~~~
        openDrawer();
        this.printerManager.printJob(new PrinterJob("\n"));

        cutFunction();

    }

    public PrintReceipt(String data,Context ctx,Order order,CashInformation cashInformation,String invoiceNum) {
        this.printerManager = PrinterManager_.getInstance_(ctx);

        this.order = order;
        this.product = order.getProducts();
        this.numberSize = 0;
        this.totalPrice = String.valueOf(order.getTotal());
        this.printData = data;
        this.cashInformation = cashInformation;
        this.invoiceNum=invoiceNum;
        this.tax=order.getMaam();
        this.beforeTax=order.getPriceBeforeTax();
        this.sourceCopyInvoice="העתק";

        printTitel();
        printSourceCopyInvoice();
        printDate();
        this.printerManager.printJob(new PrinterJob("\n"));
        startPrintProducts();
        setLien();

        printTotalProducts();
        setLien();
        printTotalPrice();
        this.printerManager.printJob(new PrinterJob("\n"));
        cutFunction();

    }
    private void printTotalProducts(){

        this.printerManager.printJob(new PrinterJob(endOfLine(threeColumns(hebrewString(this.totalStringName),String.valueOf(this.order.getTotalAmount()),this.totalPrice))));
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
        this.totalTaxbeForeString = checkWord(this.totalTaxbeForeString);
        this.totalTaxbeForeString = twoColumns(this.totalTaxbeForeString,String.valueOf(order.getMaam()));
        this.printerManager.printJob(new PrinterJob(endOfLine(this.totalTaxbeForeString)));
        this.sumBeForeTaxString = checkWord(this.sumBeForeTaxString);
        this.sumBeForeTaxString = twoColumns(this.sumBeForeTaxString,String.valueOf(order.getPriceBeforeTax()));


        printerManager.printJob(new PrinterJob(endOfLine(this.sumBeForeTaxString)));
        this.printerManager.printJob(new PrinterJob(endOfLine(" ")));

        setDoubleStrike();
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

        String str="";
        this.product = product;
        for ( Product p : this.product) {
            this.name=checkWord(p.getProductName());
            this.name=this.name.trim();
             str=threeColumns(this.name,p.getAmount(),p.getPrice());
            printerManager.printJob(new PrinterJob(endOfLine(str)));
        }
    }
    private void printTitel() {
        setBold();
        this.companeName = checkWord(this.cashInformation.getCompanyName());
        this.companeName = (centerLine(this.companeName));
        printerManager.printJob(new PrinterJob(this.companeName));
        removeBold();
        openDrawer();
        this.city = checkWord(this.cashInformation.getCity());
        this.city=centerLine(this.city);
        this.printerManager.printJob(new PrinterJob(this.city));
        this.street=checkWord(this.cashInformation.getStreet());
        this.street=centerLine(this.street);
        this.printerManager.printJob(new PrinterJob(this.street));
        this.hPString=checkWord(this.hPString);
        this.printerManager.printJob(new PrinterJob(centerLine(this.cashInformation.getH_P()+this.hPString)));
        this.printerManager.printJob(new PrinterJob(endOfLine(" ")));
    }
    private void printSourceCopyInvoice(){
        setBold();
        this.stringTax = checkWord(this.stringTax);
        this.sourceCopyInvoice = hebrewString(this.sourceCopyInvoice);
        this.stringTax = endOfLine(twoColumns(this.invoiceNum+this.stringTax,this.sourceCopyInvoice));
        printerManager.printJob(new PrinterJob(this.stringTax.trim()));
        removeBold();
        this.orderIdString = hebrewString(this.orderIdString);
        this.orderIdString = endOfLine(this.order.getIndex()+this.orderIdString);
        this.printerManager.printJob(new PrinterJob(this.orderIdString));

    }
    private void openDrawer(){
        String str = new String(openDrawer);
        printerManager.printJob(new PrinterJob(str));
    }
    private String threeColumns(String name,String amount,String price){
        int num1  = name.length();
        int num2  = 0;
        String str="";
        String str2="";

        if (num1 < 25)
        {
            num2 = 25-num1;
            num1=num2;
        }
        if (num1 +name.length() == 25)
        {
            this.name="";
            for (int i = 0 ; i < num1; i++)
            {
                str+=" ";
            }
            str+=name;
        }
        num1 = 10;
        if(amount.length()==2)
            num1-=1;
        else if(amount.length() == 3)
            num1-=2;
        else if(amount.length() == 4)
            num1-=3;



        this.amount="";
        for(int i = 0 ;i <num1;i++)
        {
            str2+=" ";
        }
        str2+=amount;
        num1=price.length();

        if(num1 > 3) num1-=1;

        else num1+=4;
        this.price="";
//        for (int i = 0;i < num1;i++)
//        {
//            this.price+="_";
//        }

        return price+str2+str;
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
        for(int i = 0 ;i <num1-7;i++)
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
    private void setDoubleStrike() {
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
            str +=" ";
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
                num = (num/2);
            }
            for(int i = 0 ; i < num;i++)
            {
                str2 +=" ";
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


            str3 = str.split(" ");
            if(str3.length == 1)
            {
                str3[0]=str3[0].trim();
                if(checkingLetters(str3[0].charAt(0)))
                    str2 = hebrewString(str3[0]);
                else
                    str2 = str3[0];
            }
            else
            {
                for(int i =0 ; i < str3.length ;i++)
                {
                    str3[i]=str3[i].trim();
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
            str2 +=" ";
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
                str2 +=" ";
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
    private void setLien(){
        String strTmp ="";
        for(int i = 0 ; i < STANDART_LENGTH;i++)
        {
            strTmp+="_";
        }
        this.printerManager.printJob(new PrinterJob(strTmp));
    }
    public void printTypePayment(){
        float change = 0;
        this.acceptedString = hebrewString(this.acceptedString);
        this.acceptedString = endOfLine(this.acceptedString);
        setDoubleStrike();
        this.printerManager.printJob(new PrinterJob(this.acceptedString));
        removeDoubleStrike();
        setLien();

        for(Payment p : this.order.getPaments()){
            if(p.getType() == 1)
            {
                this.cashPament = hebrewString(this.cashPament);
                this.cashPament = twoColumns(this.cashPament,String.valueOf(p.getAmount()));
                this.printerManager.printJob(new PrinterJob(this.cashPament));
                this.changeString = hebrewString(this.changeString);
                change += p.getChange();
            }
            else if(p.getType() == 2)
            {
                this.creditPament = hebrewString(this.creditPament);
                this.creditPament = twoColumns(this.creditPament,String.valueOf(p.getAmount()));
                this.printerManager.printJob(new PrinterJob(this.creditPament));
            }

            setLien();
        }

        this.changeString = twoColumns(this.changeString,String.valueOf(change));
        this.printerManager.printJob(new PrinterJob(this.changeString));
    }

}
