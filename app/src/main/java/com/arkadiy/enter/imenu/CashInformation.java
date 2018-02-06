package com.arkadiy.enter.imenu;

/**
 * Created by vadnu on 06/02/2018.
 */

public class CashInformation {
    private String cashId;
    private String companyName;
    private String H_P;
    private String phone;
    private String city;
    private String street;

    public CashInformation(String cashId, String companyName, String h_P, String phone, String city, String street) {
        this.cashId = cashId;
        this.companyName = companyName;
        H_P = h_P;
        this.phone = phone;
        this.city = city;
        this.street = street;
    }

    public String getCashId() {
        return cashId;
    }

    public void setCashId(String cashId) {
        this.cashId = cashId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getH_P() {
        return H_P;
    }

    public void setH_P(String h_P) {
        H_P = h_P;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
}
