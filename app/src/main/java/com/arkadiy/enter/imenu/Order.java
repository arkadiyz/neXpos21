package com.arkadiy.enter.imenu;

import java.util.ArrayList;

/**
 * Created by vadnu on 28/01/2018.
 */

public class Order {
    private int index;
    private int indexInButtons;
    private float total;
    private ArrayList<Product>products;

    public Order(int index,float total){
        this.index=index;
        this.total=total;
        this.products=new ArrayList<>();
    }

    public Order(int index,int indexInButtons){
        this.index=index;
        this.total=0;
        this.indexInButtons=indexInButtons;
        this.products=new ArrayList<>();

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
        return this.total;
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
