package com.example.healthapp.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Km")
public class km {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private float km;

    public km() {
    }

    public km(float km) {
        this.km = km;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getKm() {
        return km;
    }

    public void setKm(float km) {
        this.km = km;
    }
}
