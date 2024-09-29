package com.example.managerestaurantapp.models;

import java.io.Serializable;

public class Order implements Serializable {
    private int DishID, Quantity, DishPrice;
    private String DishName;

    public Order(int dishID, String DishName, int quantity, int dishPrice) {
        this.DishID = dishID;
        this.Quantity = quantity;
        this.DishPrice = dishPrice;
        this.DishName = DishName;
    }

    public Order() {

    }

    @Override
    public String toString() {
        return "Order{" +
                "DishID=" + DishID +
                ", Quantity=" + Quantity +
                ", DishPrice=" + DishPrice +
                ", DishName='" + DishName + '\'' +
                '}';
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public int getDishID() {
        return DishID;
    }

    public void setDishID(int dishID) {
        DishID = dishID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getDishPrice() {
        return DishPrice;
    }

    public void setDishPrice(int dishPrice) {
        DishPrice = dishPrice;
    }
}
