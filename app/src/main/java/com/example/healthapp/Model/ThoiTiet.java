package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ThoiTiet")
public class ThoiTiet implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String NgayGio;
    private String ViTri;
    private int NhietDo;
    private String UV;
    private String doam;
    private String gio;
    private String TyLeMua;

    public ThoiTiet() {
    }

    public ThoiTiet(String ngayGio, String viTri, int nhietDo, String UV, String tyLeMua) {
        NgayGio = ngayGio;
        ViTri = viTri;
        NhietDo = nhietDo;
        this.UV = UV;
        TyLeMua = tyLeMua;
    }

    public ThoiTiet(String doam, String gio) {
        this.doam = doam;
        this.gio = gio;
    }

    public String getDoam() {
        return doam;
    }

    public void setDoam(String doam) {
        this.doam = doam;
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNgayGio() {
        return NgayGio;
    }

    public void setNgayGio(String ngayGio) {
        NgayGio = ngayGio;
    }

    public int getNhietDo() {
        return NhietDo;
    }

    public void setNhietDo(int nhietDo) {
        NhietDo = nhietDo;
    }

    public String getUV() {
        return UV;
    }

    public void setUV(String UV) {
        this.UV = UV;
    }

    public String getTyLeMua() {
        return TyLeMua;
    }

    public void setTyLeMua(String tyLeMua) {
        TyLeMua = tyLeMua;
    }

    public String getViTri() {
        return ViTri;
    }

    public void setViTri(String viTri) {
        ViTri = viTri;
    }
}
