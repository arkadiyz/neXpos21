package com.arkadiy.enter.imenu;

/**
 * Created by Arkadiy on 1/4/2018.
 */

public class Product {
    private String productName;
    private String amount;
    private String price;
    private String barcode;
    private String picPath;
    private int ig_id;


    public Product(String productName, String amount, String price) {
        this.productName = productName;
        this.amount = amount;
        this.price = price;


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
}
