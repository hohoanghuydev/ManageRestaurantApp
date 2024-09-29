package com.example.managerestaurantapp.models;

public class Dish {
    private int DishID;
    private String DishName;
    private int UnitPrice;
    private int CategoryID;

    public Dish(int dishID, String dishName, int unitPrice, int categoryID) {
        DishID = dishID;
        DishName = dishName;
        UnitPrice = unitPrice;
        CategoryID = categoryID;
    }

    @Override
    public String toString() {
        return getDishName() + " - " + getUnitPrice();
    }

    public Dish() {
    }

    public int getDishID() {
        return DishID;
    }

    public void setDishID(int dishID) {
        DishID = dishID;
    }

    public String getDishName() {
        return DishName;
    }

    public void setDishName(String dishName) {
        DishName = dishName;
    }

    public int getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        UnitPrice = unitPrice;
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }
}
