package com.example.managerestaurantapp.models;

public class DiningTable {
    private int TableID, TableFloor, SeatCount;
    private String Note;

    public DiningTable(int tableID, int tableFloor, int seatCount, String note) {
        TableID = tableID;
        TableFloor = tableFloor;
        SeatCount = seatCount;
        Note = note;
    }

    public DiningTable() {
    }

    public int getTableID() {
        return TableID;
    }

    public void setTableID(int tableID) {
        TableID = tableID;
    }

    public int getTableFloor() {
        return TableFloor;
    }

    public void setTableFloor(int tableFloor) {
        TableFloor = tableFloor;
    }

    public int getSeatCount() {
        return SeatCount;
    }

    public void setSeatCount(int seatCount) {
        SeatCount = seatCount;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }
}
