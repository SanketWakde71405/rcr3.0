package com.example.rcr30;

public class Component {
    String ComponentName;
    String imageURL;
    int Available;
    int Working;
    int Non_working;


    Integer image;
    String Specifications;

    public Component(String componentName, Integer image) {
        this.ComponentName = componentName;
        this.image = image;
    }

    public Component(String componentName, String imageURL, int available, int working, int non_working) {
        ComponentName = componentName;
        this.imageURL = imageURL;
        Available = available;
        Working = working;
        Non_working = non_working;
    }

    public Component(String ComponentName, String imageURL, int Available, int Working, int Non_working, String Specifications) {
        this.ComponentName = ComponentName;
        this.imageURL = imageURL;
        this.Available = Available;
        this.Working = Working;
        this.Non_working = Non_working;
        this.Specifications = Specifications;
    }

    public String getCompName() {
        return ComponentName;
    }

    public void setCompName(String ComponentName) {
        this.ComponentName = ComponentName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getAvail() {
        return Available;
    }

    public void setAvail(int Available) {
        this.Available = Available;
    }

    public int getWork() {return Working; }

    public void setWork(int Working) {
        this.Working = Working;
    }

    public int getNon_work() {
        return Non_working;
    }

    public void setNon_work(int Non_working) {
        this.Non_working = Non_working;
    }

    public String getSpecs() {
        return Specifications;
    }

    public void setSpecs(String Specifications) {
        this.Specifications = Specifications;
    }

    public Integer getImage() { return image; }

    public void setImage(Integer image) { this.image = image; }


    public Component() {
    }




}
