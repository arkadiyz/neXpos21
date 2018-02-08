package com.arkadiy.enter.imenu;

/**
 * Created by vadnu on 07/02/2018.
 */

public class Employee {
    String name;
    int password;
    int id;

    public Employee() {
    }

    public Employee(String name,int id) {
        this.name = name;
        this.id = id;
    }

    public Employee(String name, int password, int id) {
        this.name = name;
        this.password = password;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
