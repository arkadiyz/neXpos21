package com.arkadiy.enter.imenu;

/**
 * Created by vadnu on 05/02/2018.
 */

public class Payment {
    private String type;
    private float amount;
    private String date;
    private int orderId;

    public Payment(String typePayment,float amountPayed,String date,int orderId){
        type=typePayment;
        amount=amountPayed;
        this.date=date;
        this.orderId=orderId;
    }
}
