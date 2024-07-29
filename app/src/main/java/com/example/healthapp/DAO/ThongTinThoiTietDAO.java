package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.healthapp.Model.ThongTinThoiTiet;

@Dao
public interface ThongTinThoiTietDAO {
    @Insert
    void insertThongTinThoiTiet(ThongTinThoiTiet thongTinThoiTiet);

    @Query("UPDATE ThongTinThoiTiet SET viTri = :viTri, uv = :uv, doAm = :doAm, gio=:gio WHERE ngayGio = :ngay")
    void updateThongTinThoiTiet(String viTri, String uv, String doAm, String gio, String ngay);

    @Query("SELECT * FROM ThongTinThoiTiet WHERE NgayGio = :ngayGio")
    ThongTinThoiTiet getThongTinThoiTietByDate(String ngayGio);

    @Query("UPDATE ThongTinThoiTiet SET BinhMinh = :binhMinh, HoangHon=:hoangHon WHERE ngayGio = :ngay")
    void updateThongTinThoiTiet2(String binhMinh, String hoangHon, String ngay);

    @Query("SELECT * FROM ThongTinThoiTiet ORDER BY id DESC LIMIT 1")
    ThongTinThoiTiet getLastThongTinThoiTiet();
}
