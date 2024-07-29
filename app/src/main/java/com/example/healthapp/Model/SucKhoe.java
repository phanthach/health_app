package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SucKhoe")
public class SucKhoe {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String BuocChan;
    private String Time;
    private String Kcal;
    private String Ngay;
    public SucKhoe() {
    }

    public SucKhoe(String buocChan, String time, String kcal, String ngay) {
        BuocChan = buocChan;
        Time = time;
        Kcal = kcal;
        Ngay = ngay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuocChan() {
        return BuocChan;
    }

    public void setBuocChan(String buocChan) {
        BuocChan = buocChan;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getKcal() {
        return Kcal;
    }

    public void setKcal(String kcal) {
        Kcal = kcal;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }
}
