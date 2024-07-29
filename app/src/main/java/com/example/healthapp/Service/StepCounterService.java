package com.example.healthapp.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.healthapp.DAO.kmDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.SucKhoe;
import com.example.healthapp.Model.km;
import com.example.healthapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StepCounterService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final int STEP_THRESHOLD = 22;// Ngưỡng để xác định một bước chân

    private static final long SENSOR_PAUSE_THRESHOLD = 5000;

    private boolean checkNguong = false;
    private boolean isStepCounting = false;
    private int stepCount = 0;
    private long lastStepTimestamp = 0;
    private long firstStepTimestamp = 0;
    private long timeCount =0;
    private long time1 = 0;
    private long time2 = 0;
    private boolean checkTime = false;
    private boolean checkSend = false;


    @Override
    public void onCreate() {
        super.onCreate();
        // Đăng ký BroadcastReceiver
        ScreenReceiver screenReceiver = new ScreenReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenReceiver, filter);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (accelerometer != null) {
                sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                // Không có cảm biến accelerometer, thông báo ra màn hình
                Toast.makeText(this, "Thiết bị không hỗ trợ cảm biến", Toast.LENGTH_LONG).show();
                // Thông báo ra màn hình hoặc xử lý tiếp theo tùy ý
            }
        } else {
            // Không thể truy cập được SensorManager, thông báo ra màn hình
            Log.d("StepCounterService", "Không thể truy cập được SensorManager");
            // Thông báo ra màn hình hoặc xử lý tiếp theo tùy ý
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            double acceleration = Math.sqrt(x * x + y * y + z * z);
//            Log.d("==============================================x", String.valueOf(x));
//            Log.d("==============================================y", String.valueOf(y));
//            Log.d("==============================================z", String.valueOf(z));
            // Kiểm tra xem ngưỡng gia tốc đã được vượt qua chưa
            if (acceleration >= STEP_THRESHOLD) {
                if (!isStepCounting) {
                    isStepCounting = true;
                    stepCount++;
                    checkTime=false;
                    checkSend=false;
                    firstStepTimestamp = System.currentTimeMillis();
                    Log.d("========================================count", String.valueOf(stepCount));
                }
                if (!checkNguong) {
                    lastStepTimestamp = System.currentTimeMillis();
                    checkNguong = true;
                }
            }
            else {
                isStepCounting=false;
                if(!checkTime){
                    time1 = System.currentTimeMillis();
                    checkTime=true;
                }
                if( checkNguong && time1+3000 < System.currentTimeMillis()){
                    timeCount = (time1 - lastStepTimestamp)/1000;
                    Log.d("========================================Timestamp2", String.valueOf(timeCount));
                    if(!checkSend){
                        sendStepCountBroadcast(stepCount, timeCount);
                        stepCount=0;
                        checkSend=true;
                    }
                    checkNguong=false;
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Không cần thiết cho việc đếm bước chân
    }

    private void sendStepCountBroadcast(int stepCount, long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());

        Connection connection = Connection.getInstance(getApplicationContext());
        kmDAO db2 = connection.kmDAO();
        km km =db2.getKM();
        if(km==null){
            km km1 = new km();
            km1.setKm((float) (stepCount*1.5));
            db2.insertkm(km1);
        }
        else{
            km km3 = db2.getKM();
            km3.setKm(km3.getKm()+ (float)(stepCount*1.5));
        }
        SucKhoe sucKhoe = connection.sucKhoeDAO().getSucKhoeByDate(today);
        if(sucKhoe==null){
            SucKhoe sucKhoe1 = new SucKhoe(String.valueOf(stepCount), String.valueOf(timestamp), String.valueOf(stepCount*0.06), today);
            connection.sucKhoeDAO().insertSucKhoe(sucKhoe1);
        }
        else{
            long buocchan= Integer.parseInt(sucKhoe.getBuocChan())+stepCount;
            long thoigian= Integer.parseInt(sucKhoe.getTime())+timestamp;
            float  kcal =  Float.parseFloat(sucKhoe.getKcal())+ (float)(stepCount*0.06);
            Log.d("========================================kcal", String.valueOf(kcal));
            connection.sucKhoeDAO().updateStepCount(String.valueOf(buocchan), String.valueOf(thoigian),String.valueOf(kcal), today );
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForegroundService();

        // Trả về START_STICKY để dịch vụ được khởi động lại nếu bị hủy
        return START_STICKY;
    }

    private void startForegroundService() {
        String channelId = "StepCounterServiceChannel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo NotificationChannel cho các phiên bản Android mới hơn API 26
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Dịch vụ đang chạy ngầm",
                    NotificationManager.IMPORTANCE_LOW
            );
            notificationManager.createNotificationChannel(channel);
        }
            Notification notification = new NotificationCompat.Builder(this, channelId)
                    .setContentTitle("Dịch vụ đang chạy ngầm")
                    .setSmallIcon(R.drawable.ic_notifications)
                    .build();

            // Bắt đầu dịch vụ Foreground với notification đã tạo
            startForeground(1, notification);
        }
}
