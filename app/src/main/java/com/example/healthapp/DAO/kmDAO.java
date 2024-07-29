package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthapp.Model.km;

@Dao
public interface kmDAO {
    @Insert

    void insertkm(km km);
    @Query("SELECT * FROM km ORDER BY id DESC LIMIT 1")
    km getKM();

}
