package com.arkadiy.enter.imenu;

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
    private ArrayList<Product>products;
    private ArrayList<Payment>payments;


    public Order(int index,float total){
        this.index=index;
        this.total=total;
        this.products=new ArrayList<>();
    }

    public Order(int index,int indexInButtons,String dateAndTime){
        this.index=index;
        this.total=0;
        this.indexInButtons=indexInButtons;
        this.products=new ArrayList<>();
        this.dateAndTime=dateAndTime;

    }
    public void addPayment(String type,float summ){
        payments.add(new Payment(type,summ));
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
        float t=0;
        for(Product p:products){
            t+=Float.parseFloat(p.getPrice());
        }
        return t;
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


}