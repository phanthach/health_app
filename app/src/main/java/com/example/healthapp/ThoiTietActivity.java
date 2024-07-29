package com.example.healthapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.healthapp.API.APIService;
import com.example.healthapp.DAO.ThoiTietDAO;
import com.example.healthapp.DAO.ThongTinThoiTietDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.ThoiTiet;
import com.example.healthapp.Model.ThongTinThoiTiet;
import com.example.healthapp.Response.ThongTinThoiTietResponse;
import com.example.healthapp.Response.WeatherResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThoiTietActivity extends AppCompatActivity {
    private TextView Vitri;
    private TextView nhietDo, tinhTrang, viTri, khuyenKhich;
    private TextView uv, doAm, gio, binhMinh, hoangHon;
    private ImageView imgThoiTiet;
    private ThoiTietDAO db;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable;

    private boolean toastDisplayed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thoi_tiet);
        init();

//        Connection connection = Connection.getInstance(getApplicationContext());
//        ThongTinThoiTietDAO db1 = connection.thongTinThoiTietDAO();
//        ThongTinThoiTiet thongTinThoiTiet = db1.getLastThongTinThoiTiet();
//        if(thongTinThoiTiet!=null){
//            Log.d("===============================================BinhMinh", thongTinThoiTiet.getNgayGio());
//            Log.d("===============================================HoangHon", thongTinThoiTiet.getUv());
//        }else{
//            Log.d("===============================================HoangHon", "0000000");
//        }
        runnable = new Runnable() {
            @Override
            public void run() {
                showWeatherInfo();
                CallAPIThoiTiet();
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 0000);
        onClick();
    }

    private void init(){
        Vitri = findViewById(R.id.txtViTri);
        nhietDo= findViewById(R.id.txtNhietDo);
        tinhTrang= findViewById(R.id.txtTinhTrangTroi);
        viTri= findViewById(R.id.txtViTri);
        khuyenKhich= findViewById(R.id.txtKhuyenKhich);
        imgThoiTiet = findViewById(R.id.image_thoitiet);

        uv = findViewById(R.id.txtUV);
        doAm = findViewById(R.id.txtDoAm);
        gio =findViewById(R.id.txtGio);
        binhMinh = findViewById(R.id.txtBinhMinh);
        hoangHon = findViewById(R.id.txtHoangHon);

    }
    private void onClick() {
        Vitri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ThayDoiViTri.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Check_OK")) {
            String check = intent.getStringExtra("Check_OK");
            if(check != null && check.equals("OK")){
                Toast.makeText(this, "OKE", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void CallAPIThoiTiet() {
        //    Link API: http://api.weatherapi.com/v1/current.json?Key=4239a7bcd21e4b1eb23190251240405&q=HaNoi
        Connection connection = Connection.getInstance(getApplicationContext());
        db = connection.thoiTietDAO();
        ThoiTiet tt = db.getLastThoiTiet();
        ThoiTiet tt2 = new ThoiTiet();
        String vitri = "";
        if(tt==null){
            ThoiTiet thoiTiet = new ThoiTiet("0","Ha Noi",0,"0","0");
            db.InsertThoiTiet(thoiTiet);
            tt2=db.getLastThoiTiet();
            vitri+=tt2.getViTri();
        }
        else{
            vitri+=tt.getViTri();
        }
        APIService.apiService.converWeatherData("334c3a7a4bcd450d8f593000242305",vitri, "vi").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toastDisplayed = false;
                    WeatherResponse weatherResponse = response.body();
                    ThoiTiet thoiTiet = createThoiTietFromResponse(weatherResponse);
//                    Database thoi tiet
                    Connection connection = Connection.getInstance(getApplicationContext());
                    db = connection.thoiTietDAO();
                    db.InsertThoiTiet(thoiTiet);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
    }
    private void showWeatherInfo() {
        // Hiển thị thông tin nhiệt độ và UV lên giao diện
        Connection connection = Connection.getInstance(getApplicationContext());
        ThongTinThoiTietDAO db1 = connection.thongTinThoiTietDAO();
        ThongTinThoiTiet thongTinThoiTiet = db1.getLastThongTinThoiTiet();

        ThoiTietDAO db = connection.thoiTietDAO();
        ThoiTiet thoiTiet1 = db.getLastThoiTiet();

        if(thoiTiet1!=null){
            nhietDo.setText(String.valueOf(thoiTiet1.getNhietDo()) + "°C");
            if(Float.parseFloat(thoiTiet1.getUV())>=0.0 && Float.parseFloat(thoiTiet1.getUV())<=2.0){
                uv.setText("Thấp");
                khuyenKhich.setText("Khuyến khích vận động");
            }
            else if(Float.parseFloat(thoiTiet1.getUV())>2.0 && Float.parseFloat(thoiTiet1.getUV())<=8.0){
                uv.setText("Trung Bình");
                khuyenKhich.setText("Khuyến khích vận động trong nhà");
            }
            else if(Float.parseFloat(thoiTiet1.getUV())>8.0 && Float.parseFloat(thoiTiet1.getUV())<=11.0){
                uv.setText("Cao");
                khuyenKhich.setText("Hạn chế ra đường");
            }
            else {
                uv.setText("Cực cao");
                khuyenKhich.setText("Chỉ ra đường khi cần thiết");
            }
            viTri.setText(thoiTiet1.getViTri());
            tinhTrang.setText(thoiTiet1.getTyLeMua());
            if(thoiTiet1.getTyLeMua().contains("Mây") ||thoiTiet1.getTyLeMua().contains("mây")){
                imgThoiTiet.setImageResource(R.drawable.co_may);
            }
            else if(thoiTiet1.getTyLeMua().contains("Mưa") ||thoiTiet1.getTyLeMua().contains("mưa")){
                imgThoiTiet.setImageResource(R.drawable.anhnenmua);
            }
            else if(thoiTiet1.getTyLeMua().contains("Nắng") || thoiTiet1.getTyLeMua().contains("nắng")){
                imgThoiTiet.setImageResource(R.drawable.anhnangnong);
            }
        }
        else{
            nhietDo.setText("30°C");
            uv.setText("Chi so UV: Thấp");
            khuyenKhich.setText("Khuyến khích vận động");
            viTri.setText("Ha Noi");
            tinhTrang.setText("Nắng");
            imgThoiTiet.setImageResource(R.drawable.anhnangnong);
        }
        if(thongTinThoiTiet!=null){
            doAm.setText(thongTinThoiTiet.getDoAm());
            gio.setText(thongTinThoiTiet.getGio()+" km/h");
            binhMinh.setText(thongTinThoiTiet.getBinhMinh());
            hoangHon.setText(thongTinThoiTiet.getHoangHon());
        }
    }

    private ThoiTiet createThoiTietFromResponse(WeatherResponse weatherResponse) {
        WeatherResponse.Location location = weatherResponse.getLocation();
        WeatherResponse.CurrentWeather currentWeather = weatherResponse.getCurrent();
        String ngayGio = location.getLocaltime();
        String viTri = location.getName();
        int nhietDo = Math.round(currentWeather.getTempC());
        String uv = String.valueOf(currentWeather.getUv());
        String tinhTrang = currentWeather.getCondition().getText();
        return new ThoiTiet(ngayGio, viTri, nhietDo, uv, tinhTrang);
    }
    private ThoiTiet createThoiTietFromResponse1(WeatherResponse weatherResponse) {
        WeatherResponse.Location location = weatherResponse.getLocation();
        WeatherResponse.CurrentWeather currentWeather = weatherResponse.getCurrent();
        String doam = String.valueOf(currentWeather.getHumidity());
        String gio = String.valueOf(currentWeather.getWind_kph());
        return new ThoiTiet(doam,gio);
    }
    private ThongTinThoiTiet createThongTinThoiTietResponse(ThongTinThoiTietResponse weatherResponse) {
        ThongTinThoiTietResponse.Astronomy astronomy = weatherResponse.getAstronomy();
        String BinhMinh = astronomy.getAstro().getSunrise();
        String HoangHon = astronomy.getAstro().getSunset();
        return new ThongTinThoiTiet(BinhMinh, HoangHon);
    }
    @Override
    public void onResume() {
        super.onResume();
        Connection connection = Connection.getInstance(getApplicationContext());
        ThongTinThoiTietDAO db1 = connection.thongTinThoiTietDAO();

        db = connection.thoiTietDAO();
        ThoiTiet tt = db.getLastThoiTiet();
        ThoiTiet tt2 = new ThoiTiet();
        String vitri = "";
        if(tt==null){
            ThoiTiet thoiTiet = new ThoiTiet("0","Ha Noi",0,"0","0");
            db.InsertThoiTiet(thoiTiet);
            tt2=db.getLastThoiTiet();
            vitri+=tt2.getViTri();
        }
        else{
            vitri+=tt.getViTri();
        }
        APIService.apiService.converWeatherData("334c3a7a4bcd450d8f593000242305",vitri, "vi").enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(retrofit2.Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toastDisplayed = false;
                    WeatherResponse weatherResponse = response.body();
                 // thoiTiet: trả về ngayGio, viTri, nhietDo, uv, tinhTrang
                    ThoiTiet thoiTiet = createThoiTietFromResponse(weatherResponse);
                 // thoiTiet1: trả về doam,gio
                    ThoiTiet thoiTiet1 = createThoiTietFromResponse1(weatherResponse);

                    Connection connection = Connection.getInstance(getApplicationContext());
                    db = connection.thoiTietDAO();
                    db.updateThoiTiet(thoiTiet.getViTri(), thoiTiet.getId());

                    ThongTinThoiTietDAO db1 = connection.thongTinThoiTietDAO();
                    ThongTinThoiTiet thongTinThoiTiet = db1.getLastThongTinThoiTiet();
                    if(thongTinThoiTiet==null){
                        db1.insertThongTinThoiTiet(new ThongTinThoiTiet(thoiTiet.getViTri(), thoiTiet.getNgayGio(), "0", "0", "0", "0", "0"));
                    }
                    db1.updateThongTinThoiTiet(thoiTiet.getViTri(), thoiTiet.getUV(), thoiTiet1.getDoam(), thoiTiet1.getGio(), thongTinThoiTiet.getNgayGio());
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {

            }
        });
        APIService.apiService.converThongTinThoiTietData("334c3a7a4bcd450d8f593000242305", vitri).enqueue(new Callback<ThongTinThoiTietResponse>() {
            @Override
            public void onResponse(Call<ThongTinThoiTietResponse> call, Response<ThongTinThoiTietResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toastDisplayed = false;
                    ThongTinThoiTietResponse weatherResponse = response.body();
                    ThongTinThoiTiet thongTinThoiTiet = createThongTinThoiTietResponse(weatherResponse);

                    Connection connection = Connection.getInstance(getApplicationContext());
                    ThongTinThoiTietDAO db1 = connection.thongTinThoiTietDAO();
                    ThongTinThoiTiet thongTinThoiTiet1 = db1.getLastThongTinThoiTiet();
                    if(thongTinThoiTiet1==null){
                        Toast.makeText(getApplicationContext(),"Không thể cập nhật dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        db1.updateThongTinThoiTiet2(thongTinThoiTiet.getBinhMinh(), thongTinThoiTiet.getHoangHon(), thongTinThoiTiet1.getNgayGio());
                    }
                }
            }

            @Override
            public void onFailure(Call<ThongTinThoiTietResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể cập nhật dữ liệu thời tiết", Toast.LENGTH_SHORT).show();
            }
        });
    }
}