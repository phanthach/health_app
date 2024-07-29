package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthapp.Model.ThoiTiet;

@Dao
public interface ThoiTietDAO {
    @Insert
    void InsertThoiTiet(ThoiTiet thoiTiet);

    @Query("SELECT * FROM ThoiTiet WHERE NgayGio = :ngayGio")
    ThoiTiet getThoiTietByDate(String ngayGio);

    @Query("SELECT * FROM ThoiTiet ORDER BY id DESC LIMIT 1")
    ThoiTiet getLastThoiTiet();

    @Query("UPDATE ThoiTiet SET ViTri=:viTri WHERE id=:id")
    void updateThoiTiet(String viTri, int id);

}
