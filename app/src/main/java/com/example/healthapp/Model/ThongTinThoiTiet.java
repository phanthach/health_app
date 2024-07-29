package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ThongTinThoiTiet")
public class ThongTinThoiTiet implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String viTri;
    private String ngayGio;
    private String uv;
    private String doAm;
    private String gio;
    private String BinhMinh;
    private String HoangHon;

    public ThongTinThoiTiet(String viTri, String ngayGio, String uv, String doAm, String gio, String binhMinh, String hoangHon) {
        this.viTri = viTri;
        this.ngayGio = ngayGio;
        this.uv = uv;
        this.doAm = doAm;
        this.gio = gio;
        BinhMinh = binhMinh;
        HoangHon = hoangHon;
    }

    public ThongTinThoiTiet(String viTri, String ngayGio, String uv, String doAm, String gio) {
        this.viTri = viTri;
        this.ngayGio = ngayGio;
        this.uv = uv;
        this.doAm = doAm;
        this.gio = gio;
    }

    public ThongTinThoiTiet(String binhMinh, String hoangHon) {
        BinhMinh = binhMinh;
        HoangHon = hoangHon;
    }

    public ThongTinThoiTiet() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getViTri() {
        return viTri;
    }

    public void setViTri(String viTri) {
        this.viTri = viTri;
    }

    public String getNgayGio() {
        return ngayGio;
    }

    public void setNgayGio(String ngayGio) {
        this.ngayGio = ngayGio;
    }

    public String getUv() {
        return uv;
    }

    public void setUv(String uv) {
        this.uv = uv;
    }

    public String getDoAm() {
        return doAm;
    }

    public void setDoAm(String doAm) {
        this.doAm = doAm;
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public String getBinhMinh() {
        return BinhMinh;
    }

    public void setBinhMinh(String binhMinh) {
        BinhMinh = binhMinh;
    }

    public String getHoangHon() {
        return HoangHon;
    }

    public void setHoangHon(String hoangHon) {
        HoangHon = hoangHon;
    }
}
