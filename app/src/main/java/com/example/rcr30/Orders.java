package com.example.rcr30;

import java.util.ArrayList;

public class Orders {

    ArrayList<CartItem> cartItems;
    String OrderDate;
    String ReturningDate;
    String ReturnedDate;




    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getReturningDate() {
        return ReturningDate;
    }

    public void setReturningDate(String returningDate) {
        ReturningDate = returningDate;
    }

    public String getReturnedDate() {
        return ReturnedDate;
    }

    public void setReturnedDate(String returnedDate) {
        ReturnedDate = returnedDate;
    }

    public ArrayList<String> getComponentList() {
        return componentList;
    }

    public void setComponentList(ArrayList<String> componentList) {
        this.componentList = componentList;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getReturningStatus() {
        return ReturningStatus;
    }

    public void setReturningStatus(String returningStatus) {
        ReturningStatus = returningStatus;
    }

    ArrayList<String> componentList;
    String UID;
    String ReturningStatus;

    public Orders() {
    }


    public Orders(ArrayList<CartItem> cartItems, String orderDate, String returningDate, ArrayList<String> componentList, String UID, String returningStatus) {
        this.cartItems = cartItems;
        OrderDate = orderDate;
        ReturningDate = returningDate;
        this.componentList = componentList;
        this.UID = UID;
        ReturningStatus = returningStatus;
    }

    public Orders(ArrayList<CartItem> cartItems, String orderDate, String returningDate, ArrayList<String> componentList, String returningStatus) {
        this.cartItems = cartItems;
        OrderDate = orderDate;
        ReturningDate = returningDate;
        this.componentList = componentList;
        ReturningStatus = returningStatus;
    }


}
