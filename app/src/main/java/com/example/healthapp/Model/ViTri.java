package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ViTri")
public class ViTri implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String TenTinh;
    private String vitri;

    public ViTri() {
    }

    public ViTri(String tenTinh, String vitri) {
        TenTinh = tenTinh;
        this.vitri = vitri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenTinh() {
        return TenTinh;
    }

    public void setTenTinh(String tenTinh) {
        TenTinh = tenTinh;
    }

    public String getVitri() {
        return vitri;
    }

    public void setVitri(String vitri) {
        this.vitri = vitri;
    }
}
