package com.arkadiy.enter.imenu;

/**
 * Created by vadnu on 05/02/2018.
 */

public class Payment {
    private int type;
    private float amount;
    private String date;
    private int orderId;
    private String cardCompany;

    public Payment(int typePayment, float amountPayed, String date, int orderId, String cardCompany){
        type=typePayment;
        amount=amountPayed;
        this.date=date;
        this.orderId=orderId;
        this.cardCompany=cardCompany;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getCardCompany() {
        return cardCompany;
    }

    public void setCardCompany(String cardCompany) {
        this.cardCompany = cardCompany;
    }
}
