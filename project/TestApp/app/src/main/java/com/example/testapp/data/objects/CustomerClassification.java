package com.example.testapp.data.objects;

public class CustomerClassification {

    private String name;
    private String description;
    private int classificationId;
    private int classificationType;

    public CustomerClassification(String name, int type, int classificationId) {
        this.name = name;
        this.classificationType = type;
        this.classificationId = classificationId;
        this.description = "Example text description";
    }

    public CustomerClassification(String name, String description, int type, int classificationId){
        this.name = name;
        this.description = description;
        this.classificationType = type;
        this.classificationId = classificationId;
    }

    public String getName(){
        return this.name;
    }

    public int getType(){
        return this.classificationType;
    }

    public String getDescription(){
        return this.description;
    }

    public int getClassificationId(){
        return this.classificationId;
    }
}
