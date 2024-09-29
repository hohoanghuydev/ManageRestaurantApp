package com.example.managerestaurantapp.models;

public class TableDish {
    private int ServiceID, DishID, Quantity, UnitPrice;
    private String Note;

    @Override
    public String toString() {
        return "TableDish{" +
                "ServiceID=" + ServiceID +
                ", DishID=" + DishID +
                ", Quantity=" + Quantity +
                ", UnitPrice=" + UnitPrice +
                ", Note='" + Note + '\'' +
                '}';
    }

    public TableDish(int serviceID, int dishID, int quantity, int unitPrice, String note) {
        ServiceID = serviceID;
        DishID = dishID;
        Quantity = quantity;
        UnitPrice = unitPrice;
        Note = note;
    }

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
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

    public int getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
