package com.arkadiy.enter.imenu;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Created by vadnu on 28/01/2018.
 */

public class Order {
    private int index;
    private int indexInButtons;
    private float total;
    private double totalTax=0;
    private String dateAndTime;
    private ArrayList<Product>products=null;
    private ArrayList<Payment>payments;
    private float maam=0;
    private int totalAmount=0;



    public Order(int index, float total){
        this.index=index;
        this.total=total;
        this.products=new ArrayList<>();
        this.payments=new ArrayList<>();
    }

    public Order(int index,int indexInButtons,String dateAndTime){
        this.index=index;
        this.total=0;
        this.indexInButtons=indexInButtons;
        this.products=new ArrayList<>();
        this.dateAndTime=dateAndTime;
        this.payments=new ArrayList<>();


    }
    public void addPayment(int type,float summ,String dateAndTime,int orderIndex,String cardCompany){
        payments.add(new Payment(type,summ,dateAndTime,orderIndex,cardCompany));
    }

    public String getDateAndTime()
    {
        return this.dateAndTime;
    }

    public void addToProducts(Product product){
        this.products.add(product);
        addToTotal(product);
    }

    public void addToTotal(Product product){
        this.total+=Float.parseFloat(product.getPrice());
    }

    public int getIndex(){
        return this.index;
    }

    public float getTotal(){
        float t= (float) 0.0;
        for(Product p:products){
            t+=Float.parseFloat(p.getPrice());
        }
        return (this.total=t);
    }

    public ArrayList<Product> getProducts(){
        return this.products;
    }

    public void setTotal(float total){
        this.total=total;
    }

    public void setProducts(ArrayList<Product> products){

        this.products=products;
    }


    public void clearProducts(){
        this.products.clear();
        this.total=0;
    }

    public void setIndex(int index){
        this.index=index;
    }


    public float getMaam()
    {
        NumberFormat formatter = new DecimalFormat("##.00");
        this.maam = Float.parseFloat(formatter.format(maam=getTotal()*(float)0.17));
        return this.maam;
    }
    public float getPriceBeforeTax(){
        return (this.total-maam);
    }
    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount += totalAmount;
    }
    public ArrayList<Payment> getPaments()
    {
        return this.payments;
    }
    public Payment getPament(int index)
    {
        return this.payments.get(index);
    }



}