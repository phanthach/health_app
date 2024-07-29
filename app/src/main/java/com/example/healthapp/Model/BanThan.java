package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "BanThan")
public class BanThan implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String hoVaTen;
    private String gioiTinh;
    private String chieuCao;
    private String canNang;
    private String ngaySinh;

    private String mucDoVanDong;
    private String BMI;
    private String BMR;
    private String mucTieu;

    private byte[] avatar;

    public BanThan() {
    }

    public BanThan(String hoVaTen, String gioiTinh, String chieuCao, String canNang, String ngaySinh, String mucDoVanDong, String BMI, String BMR, String mucTieu, byte[] avatar) {
        this.hoVaTen = hoVaTen;
        this.gioiTinh = gioiTinh;
        this.chieuCao = chieuCao;
        this.canNang = canNang;
        this.ngaySinh = ngaySinh;
        this.mucDoVanDong = mucDoVanDong;
        this.BMI = BMI;
        this.BMR = BMR;
        this.mucTieu = mucTieu;
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getChieuCao() {
        return chieuCao;
    }

    public void setChieuCao(String chieuCao) {
        this.chieuCao = chieuCao;
    }

    public String getCanNang() {
        return canNang;
    }

    public void setCanNang(String canNang) {
        this.canNang = canNang;
    }

    public String getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getMucDoVanDong() {
        return mucDoVanDong;
    }

    public void setMucDoVanDong(String mucDoVanDong) {
        this.mucDoVanDong = mucDoVanDong;
    }

    public String getBMI() {
        return BMI;
    }

    public void setBMI(String BMI) {
        this.BMI = BMI;
    }

    public String getBMR() {
        return BMR;
    }

    public void setBMR(String BMR) {
        this.BMR = BMR;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getMucTieu() {
        return mucTieu;
    }

    public void setMucTieu(String mucTieu) {
        this.mucTieu = mucTieu;
    }
}
