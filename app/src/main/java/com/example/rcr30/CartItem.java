package com.example.rcr30;

public class CartItem {
    String ComponentName;
    String imageUrl;
    int Quantity;

    public CartItem(String componentName, String imageUrl,int quantity) {
        ComponentName = componentName;
        this.imageUrl = imageUrl;
        Quantity = quantity;
    }

    public CartItem() {
    }




    public String getComponentName() {
        return ComponentName;
    }

    public void setComponentName(String componentName) {
        ComponentName = componentName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }




    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

}
