package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthapp.Model.ThoiGianSuDungDienThoai;

@Dao
public interface ThoiGianSuDungDienThoaiDAO {
    @Insert
    void insertThoiGianSuDungDienThoai(ThoiGianSuDungDienThoai thoiGianSuDungDienThoai);

    @Query("SELECT * FROM ThoiGianSuDungDienThoai WHERE Ngay = :ngay")
    ThoiGianSuDungDienThoai ThoiGianSuDungDienThoaiByDate(String ngay);

    @Query("UPDATE ThoiGianSuDungDienThoai SET Phut = :phut WHERE Ngay = :ngay")
    void updateTime(String phut, String ngay);

    @Query("SELECT * FROM ThoiGianSuDungDienThoai ORDER BY id DESC LIMIT 1 OFFSET 1")
    ThoiGianSuDungDienThoai getThoiGianHomQua();

    @Query("SELECT * FROM ThoiGianSuDungDienThoai ORDER BY CAST(Phut AS UNSIGNED) LIMIT 1 OFFSET 1")
    ThoiGianSuDungDienThoai thanhTinhSDDT();


    @Query("SELECT AVG(CAST(Phut AS UNSIGNED)) FROM ThoiGianSuDungDienThoai WHERE Ngay BETWEEN :fromDate AND :toDate")
    long getSuDungDienThoai(String fromDate, String toDate);
}
