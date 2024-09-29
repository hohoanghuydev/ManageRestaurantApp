package com.example.managerestaurantapp.models;


public class MonAn {
    public MonAn(int maMon, String tenMon, int giaMon, int maLoaiMon) {
        MaMon = maMon;
        TenMon = tenMon;
        GiaMon = giaMon;
        MaLoaiMon = maLoaiMon;
    }

    public int getMaMon() {
        return MaMon;
    }

    public void setMaMon(int maMon) {
        MaMon = maMon;
    }

    public String getTenMon() {
        return TenMon;
    }

    public void setTenMon(String tenMon) {
        TenMon = tenMon;
    }

    public int getGiaMon() {
        return GiaMon;
    }

    public void setGiaMon(int giaMon) {
        GiaMon = giaMon;
    }

    public int getMaLoaiMon() {
        return MaLoaiMon;
    }

    public void setMaLoaiMon(int maLoaiMon) {
        MaLoaiMon = maLoaiMon;
    }

    private int MaMon;
    private String TenMon;
    private int GiaMon;

    public MonAn() {
    }
    private int MaLoaiMon;



}