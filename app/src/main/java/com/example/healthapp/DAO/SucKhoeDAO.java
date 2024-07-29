package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthapp.Model.SucKhoe;

import java.util.List;

@Dao
public interface SucKhoeDAO {
    @Insert
    void insertSucKhoe(SucKhoe sucKhoe);

    @Query("SELECT * FROM SucKhoe WHERE Ngay = :ngay")
    SucKhoe getSucKhoeByDate(String ngay);

    @Query("UPDATE SucKhoe SET BuocChan = :stepCount, Time = :thoiGian, Kcal = :kcal WHERE ngay = :ngay")
    void updateStepCount(String stepCount, String thoiGian, String kcal, String ngay);

    @Query("SELECT * FROM SucKhoe ORDER BY CAST(BuocChan AS UNSIGNED) DESC LIMIT 1")
    SucKhoe thanhTinhBuocChan();

    @Query("SELECT * FROM SucKhoe ORDER BY CAST(Kcal AS UNSIGNED) DESC LIMIT 1")
    SucKhoe thanhTinhKcal();

    @Query("SELECT AVG(CAST(Time AS UNSIGNED)) FROM SucKhoe WHERE ngay BETWEEN :fromDate AND :toDate")
    long getVanDong(String fromDate, String toDate);
    @Query("SELECT * FROM SucKhoe ORDER BY id DESC LIMIT 7")
    List<SucKhoe> getLast7BuocChan();

    @Query("SELECT * FROM SucKhoe ORDER BY id DESC LIMIT 7")
    List<SucKhoe> getLast7Time();

    @Query("SELECT * FROM SucKhoe ORDER BY id DESC LIMIT 7")
    List<SucKhoe> getLast7Kcal();
}
