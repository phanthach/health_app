package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ThoiGianSuDungDienThoai")
public class ThoiGianSuDungDienThoai implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String Ngay;

    private String Phut;

    public ThoiGianSuDungDienThoai() {
    }

    public ThoiGianSuDungDienThoai(String ngay, String phut) {
        Ngay = ngay;
        this.Phut = phut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNgay() {
        return Ngay;
    }

    public void setNgay(String ngay) {
        Ngay = ngay;
    }

    public String getPhut() {
        return Phut;
    }

    public void setPhut(String phut) {
        this.Phut = phut;
    }
}
