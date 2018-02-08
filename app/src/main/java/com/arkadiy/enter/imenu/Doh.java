package com.arkadiy.enter.imenu;

import java.util.ArrayList;

/**
 * Created by vadnu on 07/02/2018.
 */

public class Doh {
    public String startDateTime;
    public String finishDateTime;
    public ArrayList<Payment> cash;
    public ArrayList<Payment> visa;
    public ArrayList<Payment>cards;
    public float totalInTheDraw=0;
    public float totalCash=0;

    public float getTotalCash() {
        return totalCash;
    }

    public float getTotalVisa() {
        return totalVisa;
    }

    public float totalVisa=0;

    public float getTotalInTheDraw() {
        return totalInTheDraw;
    }

    public void addToTotal (float summ){
        this.totalInTheDraw+=summ;
    }

    public void addToTotalCash (float summ){
        this.totalCash+=summ;
    }

    public void addToTotalVisa(float summ){
        this.totalVisa+=summ;
    }

    public Doh() {
        cash=new ArrayList<>();
        visa=new ArrayList<>();
        cards=new ArrayList<>();
    }

    public void addToCash(Payment payment){
        this.cash.add(payment);
    }
    public void addToVisa(Payment payment){
        this.visa.add(payment);

    }

    public void setFinishDateTime(String finishDateTime) {
        this.finishDateTime = finishDateTime;
    }

    public void setStartDateTime(String startDateTime) {

        this.startDateTime = startDateTime;
    }

    public String getStartDateTime() {

        return startDateTime;
    }

    public String getFinishDateTime() {
        return finishDateTime;
    }

    public ArrayList<Payment> getCash() {
        return cash;
    }

    public ArrayList<Payment> getVisa() {
        return visa;
    }

    public ArrayList<Payment> getCards() {
        return cards;
    }

    public void addToCards(Payment payment){
        this.cards.add(payment);


    }

}
