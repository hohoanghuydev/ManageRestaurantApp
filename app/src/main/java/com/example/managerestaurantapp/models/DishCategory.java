package com.example.managerestaurantapp.models;

public class DishCategory {
    private int CategoryID;
    private String CategoryName;

    public DishCategory() {
    }

    public DishCategory(int categoryID, String categoryName) {
        CategoryID = categoryID;
        CategoryName = categoryName;
    }

    @Override
    public String toString() {
        return getCategoryName();
    }

    public int getCategoryID() {
        return CategoryID;
    }

    public void setCategoryID(int categoryID) {
        CategoryID = categoryID;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
