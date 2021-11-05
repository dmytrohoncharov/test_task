package com.example.testapp.data.objects;

import java.util.Date;

public class Customer {

    private String name;
    private String nip;
    private String city;
    private Date date;
    private int customerId;
    private int classificationId;

    public Customer(String name, Date date, int classificationId){
        this.name = name;
        this.date = date;
        this.classificationId = classificationId;
        this.nip = "0123456789";
        this.city = "Warsaw";
        this.customerId = 0;
    }

    public Customer(String name, String nip, String city, Date date, int customerId, int classificationId) {
        this.name = name;
        this.nip = nip;
        this.city = city;
        this.date = date;
        this.customerId = customerId;
        this.classificationId =classificationId;
    }

    public String getName(){
        return this.name;
    }

    public String getNip(){
        return this.nip;
    }

    public String getCity(){
        return this.city;
    }

    public Date getDate(){
        return this.date;
    }

    public int getCustomerId(){
        return customerId;
    }

    public int getClassificationId(){
        return classificationId;
    }
}
