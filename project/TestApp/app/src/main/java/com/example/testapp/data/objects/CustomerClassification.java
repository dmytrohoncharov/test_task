package com.example.testapp.data.objects;

public class CustomerClassification {

    private String name;
    private String description;
    private int classificationId;

    public CustomerClassification(String name, int classificationId) {
        this.name = name;
        this.classificationId =classificationId;
        this.description = "Example text description";
    }

    public CustomerClassification(String name, String description, int classificationId){
        this.name = name;
        this.description = description;
        this.classificationId = classificationId;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public int getClassificationId(){
        return this.classificationId;
    }
}
