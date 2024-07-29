package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthapp.Model.BanThan;

@Dao
public interface BanThanDAO {

    @Insert
    void insertThongTin(BanThan banThan);

    @Query("SELECT * FROM BanThan ORDER BY id DESC LIMIT 1")
    BanThan getBanThan();
}
