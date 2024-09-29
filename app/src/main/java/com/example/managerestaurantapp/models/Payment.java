package com.example.managerestaurantapp.models;

import java.sql.Date;

public class Payment {
    private int ServiceID, EmployeeID, TotalAmount, Discount;
    private Date PaymentTime;

    @Override
    public String toString() {
        return "Payment{" +
                "ServiceID=" + ServiceID +
                ", EmployeeID=" + EmployeeID +
                ", TotalAmount=" + TotalAmount +
                ", Discount=" + Discount +
                ", PaymentTime=" + PaymentTime +
                '}';
    }

    public int getServiceID() {
        return ServiceID;
    }

    public void setServiceID(int serviceID) {
        ServiceID = serviceID;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public Date getPaymentTime() {
        return PaymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        PaymentTime = paymentTime;
    }

    public Payment() {
    }

    public Payment(int serviceID, int employeeID, int totalAmount, int discount, Date paymentTime) {
        ServiceID = serviceID;
        EmployeeID = employeeID;
        TotalAmount = totalAmount;
        Discount = discount;
        PaymentTime = paymentTime;
    }
}
