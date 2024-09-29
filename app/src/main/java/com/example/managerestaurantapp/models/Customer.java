package com.example.managerestaurantapp.models;

public class Customer {
    int customerId;
    String customerName,adress,phoneNumber;

    public Customer(int customerId, String customerName, String adress, String phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.adress = adress;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
