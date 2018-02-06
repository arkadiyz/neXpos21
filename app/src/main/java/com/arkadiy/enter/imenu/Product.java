package com.arkadiy.enter.imenu;

/**
 * Created by Arkadiy on 1/4/2018.
 */

public class Product {
    private String productName=null;
    private String amount=null;
    private String price=null;
    private String barcode=null;
    private String picPath=null;
    private int ig_id=0;
    private int itemId;


    public Product(String productName, String amount, String price,String barcode,String path,int id) {
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.barcode=barcode;
        this.picPath=path;
        this.ig_id=id;
        this.itemId=itemId;


    }

    public Product(String productName, String price,String barcode,String path,int id,int itemId) {
        this.productName = productName;
        this.price = price;
        this.barcode=barcode;
        this.picPath=path;
        this.ig_id=id;
        this.itemId=itemId;

    }
    public Product(String productName, String amount, String price,int id) {
        this.productName = productName;
        this.amount = amount;
        this.price = price;
        this.itemId=id;
    }

    public Product(String productName) {
        this.productName = productName;
        this.itemId=0;
    }


    public Product() {

    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getBarcode()
    {
        return this.barcode;
    }

    public void setBarcode(String b)
    {
        this.barcode=b;
    }

    public String getPicPath(){
        return this.picPath;
    }

    public void setPicPath(String path){
        this.picPath=path;
    }

    public int getItemId(){
        return this.itemId;
    }
}
