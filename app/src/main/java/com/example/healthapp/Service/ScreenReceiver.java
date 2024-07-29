package com.example.healthapp.Service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.ThoiGianSuDungDienThoai;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScreenReceiver extends BroadcastReceiver {
    private long TimeStart =0;
    private long TimeEnd = 0;
    private long Count =0;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            // Màn hình đã được mở
            TimeStart = System.currentTimeMillis();
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            // Màn hình đã được tắt
            TimeEnd = System.currentTimeMillis();
            if(TimeStart==0){
                TimeStart = System.currentTimeMillis();
            }
            Count = (TimeEnd - TimeStart)/1000;
            Log.d("==================================================Count",String.valueOf(Count) );
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String today = dateFormat.format(new Date());

            Connection connection = Connection.getInstance(context);

            ThoiGianSuDungDienThoai thoiGianSuDungDienThoai = connection.thoiGianSuDungDienThoaiDAO().ThoiGianSuDungDienThoaiByDate(today);
            if(thoiGianSuDungDienThoai==null){
                ThoiGianSuDungDienThoai thoiGianSuDungDienThoai1 = new ThoiGianSuDungDienThoai(today, "0");
                connection.thoiGianSuDungDienThoaiDAO().insertThoiGianSuDungDienThoai(thoiGianSuDungDienThoai1);
            }
            else{
                long phut = Integer.parseInt(thoiGianSuDungDienThoai.getPhut())+Count;
                connection.thoiGianSuDungDienThoaiDAO().updateTime(String.valueOf(phut), today);
                Log.d("==================================================Time",thoiGianSuDungDienThoai.getPhut() );
            }
        }

    }
}
