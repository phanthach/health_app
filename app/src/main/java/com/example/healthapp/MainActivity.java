package com.example.healthapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.healthapp.Adapter.ViewPagerAdapter;
import com.example.healthapp.DAO.ThoiTietDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.ThoiTiet;
import com.example.healthapp.Service.StepCounterService;
import com.example.healthapp.Service.WeatherAlarmReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationView;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView = findViewById(R.id.btNavigation);
        mViewPager= findViewById(R.id.vPager);

        setDailyWeatherAlarm(this);

        Intent serviceIntent = new Intent(this, StepCounterService.class);
        startService(serviceIntent);

        setUpViewPager();
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.lichsu:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.hoso:
                        mViewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    private void setUpViewPager() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.lichsu).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.hoso).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void setDailyWeatherAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, WeatherAlarmReceiver.class);
        Connection connection = Connection.getInstance(getApplicationContext());
        ThoiTietDAO db = connection.thoiTietDAO();
        ThoiTiet thoiTiet = db.getLastThoiTiet();
        String nhietdo ="";
        String uv ="";
        if(thoiTiet!=null){
            nhietdo+=thoiTiet.getNhietDo();
            uv+=thoiTiet.getUV();
        }
        // Truyền dữ liệu thời tiết qua Intent
        intent.putExtra("NhietDo", nhietdo); // Thay bằng giá trị thực tế từ nguồn dữ liệu của bạn
        intent.putExtra("UV", uv); // Thay bằng giá trị thực tế từ nguồn dữ liệu của bạn

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Thiết lập thời gian 7h sáng hàng ngày
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        // Kiểm tra nếu thời gian thiết lập nhỏ hơn thời gian hiện tại thì đặt vào ngày hôm sau
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Đặt báo thức hàng ngày
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}