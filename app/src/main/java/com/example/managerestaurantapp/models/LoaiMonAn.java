package com.example.managerestaurantapp.models;

import org.json.JSONObject;

public class LoaiMonAn extends JSONObject {
    public int getMaLoaiMonAn() {
        return maLoaiMonAn;
    }

    public void setMaLoaiMonAn(int maLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
    }

    int maLoaiMonAn;

    public LoaiMonAn() {
    }

    public LoaiMonAn(int maLoaiMonAn, String tenLoaiMonAn) {
        this.maLoaiMonAn = maLoaiMonAn;
        this.tenLoaiMonAn = tenLoaiMonAn;
    }

    String tenLoaiMonAn;



    public  String getTenLoaiMonAn() {
        return tenLoaiMonAn;
    }

    public void setTenLoaiMonAn(String tenLoaiMonAn) {
        this.tenLoaiMonAn = tenLoaiMonAn;
    }
}
