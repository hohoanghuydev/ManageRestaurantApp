package com.example.managerestaurantapp.models;

import java.sql.Date;

public class TableService {
    private int ServiceID, TableID, CustomerID;
    private Date StartTime;

    public TableService(int serviceID, int tableID, int customerID, Date startTime) {
        ServiceID = serviceID;
        TableID = tableID;
        CustomerID = customerID;
        StartTime = startTime;
    }

    public TableService() {
    }

    @Override
    public String toString() {
        return "TableService{" +
                "ServiceID=" + ServiceID +
                '}';
    }

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
    }

    public int getTableID() {
        return TableID;
    }

    public void setTableID(int tableID) {
        TableID = tableID;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date startTime) {
        StartTime = startTime;
    }
}
