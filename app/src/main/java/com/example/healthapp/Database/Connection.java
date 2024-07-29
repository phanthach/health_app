package com.example.healthapp.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.healthapp.DAO.BanThanDAO;
import com.example.healthapp.DAO.SucKhoeDAO;
import com.example.healthapp.DAO.ThoiGianSuDungDienThoaiDAO;
import com.example.healthapp.DAO.ThoiTietDAO;
import com.example.healthapp.DAO.ThongTinThoiTietDAO;
import com.example.healthapp.DAO.ViTriDAO;
import com.example.healthapp.DAO.kmDAO;
import com.example.healthapp.Model.BanThan;
import com.example.healthapp.Model.SucKhoe;
import com.example.healthapp.Model.ThoiGianSuDungDienThoai;
import com.example.healthapp.Model.ThoiTiet;
import com.example.healthapp.Model.ThongTinThoiTiet;
import com.example.healthapp.Model.ViTri;
import com.example.healthapp.Model.km;

@Database(entities = {ThoiTiet.class, SucKhoe.class, ThoiGianSuDungDienThoai.class, BanThan.class, ViTri.class, ThongTinThoiTiet.class, km.class}, version = 1)
public abstract class Connection extends RoomDatabase {
    private static final String DATABASE_NAME="Health.db";
    private static Connection instance;

    public static synchronized Connection getInstance(Context context){
        if(instance==null){
//                instance = Room.databaseBuilder(context.getApplicationContext(), BaiHatDatabase.class, DATABASE_NAME).allowMainThreadQueries().build();
            instance = Room.databaseBuilder(context.getApplicationContext(), Connection.class, DATABASE_NAME).allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract ThoiTietDAO thoiTietDAO();
    public abstract SucKhoeDAO sucKhoeDAO();

    public abstract ThoiGianSuDungDienThoaiDAO thoiGianSuDungDienThoaiDAO();

    public abstract BanThanDAO banThanDAO();
    public abstract ViTriDAO viTriDAO();
    public abstract ThongTinThoiTietDAO thongTinThoiTietDAO();
    public  abstract kmDAO kmDAO();
}
