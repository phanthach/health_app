package com.example.healthapp.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.healthapp.API.APIService;
import com.example.healthapp.ChayBoActivity;
import com.example.healthapp.DAO.BanThanDAO;
import com.example.healthapp.DAO.SucKhoeDAO;
import com.example.healthapp.DAO.ThoiGianSuDungDienThoaiDAO;
import com.example.healthapp.DAO.ThoiTietDAO;
import com.example.healthapp.DapXeActivity;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.DiBoActivity;
import com.example.healthapp.Model.BanThan;
import com.example.healthapp.Model.SucKhoe;
import com.example.healthapp.Model.ThoiGianSuDungDienThoai;
import com.example.healthapp.Model.ThoiTiet;
import com.example.healthapp.R;
import com.example.healthapp.Response.WeatherResponse;
import com.example.healthapp.ThoiTietActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class fragment_home extends Fragment {
//    CardView thời tiết
    private TextView nhietDo, tinhTrang, viTri, tiaUv, khuyenKhich, ttkm;
    private ImageView imgThoiTiet;

//    CardView Đếm số bước chân
    private TextView soBuoc,ThoiGian, Kcal;
    private TextView dibo, chaybo, dapxe;
    private TextView thoigiansddienthoai, NhieuHonHomQua,ChuYSucKhoe;
    private ThoiTietDAO db;

    private Handler handler = new Handler(Looper.getMainLooper());

    private Runnable runnable;

    private boolean toastDisplayed = false;

    private ImageView dibo_image;
    private TextView MucTieu,KcalNgay;

    private CardView cardThoiTiet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nhietDo= view.findViewById(R.id.txtNhietDo);
        tinhTrang= view.findViewById(R.id.txtTinhTrangTroi);
        viTri= view.findViewById(R.id.txtViTri);
        tiaUv= view.findViewById(R.id.txtChiSoUV);
        khuyenKhich= view.findViewById(R.id.txtKhuyenKhich);
        imgThoiTiet = view.findViewById(R.id.image_thoitiet);
        cardThoiTiet = view.findViewById(R.id.cardThoiTiet);
        dibo = view.findViewById(R.id.txtDiBo);
        chaybo = view.findViewById(R.id.txtChayBo);
        dapxe = view.findViewById(R.id.txtDapXe);

        soBuoc = view.findViewById(R.id.txtSoBuoc);
        ThoiGian = view.findViewById(R.id.txtThoiGian);
        Kcal = view.findViewById(R.id.txtKcal);

        thoigiansddienthoai = view.findViewById(R.id.txtThoiGianSuĐungienThoai);
        NhieuHonHomQua = view.findViewById(R.id.txtNhieuHonHomQua);
        ChuYSucKhoe = view.findViewById(R.id.txtChuYSucKhoe);

        MucTieu = view.findViewById(R.id.txtMucTieu);
        KcalNgay = view.findViewById(R.id.txtKcalNgay);

        onClick();

        runnable = new Runnable() {
            @Override
            public void run() {
                setSucKhoe();
                CallAPIThoiTiet();
                showWeatherInfo();
                setThoiGian();
                setMucTieu();
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 0000);

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Loại bỏ Runnable khi fragment bị hủy
        handler.removeCallbacks(runnable);
    }
    private void onClick(){
        cardThoiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThoiTietActivity.class);
                startActivity(intent);
            }
        });
        dibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DiBoActivity.class);
                startActivity(intent);
            }
        });
        chaybo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChayBoActivity.class);
                startActivity(intent);
            }
        });
        dapxe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DapXeActivity.class);
                startActivity(intent);
            }
        });
    }
    private void CallAPIThoiTiet() {
        //    Link API: http://api.weatherapi.com/v1/current.json?Key=4239a7bcd21e4b1eb23190251240405&q=HaNoi
        Connection connection = Connection.getInstance(getContext());
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
                    Connection connection = Connection.getInstance(getContext());
                    db = connection.thoiTietDAO();
                    ThoiTiet thoiTiet1 = db.getThoiTietByDate(thoiTiet.getNgayGio());
                    if(thoiTiet1==null){
                        db.InsertThoiTiet(thoiTiet);
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                if (!toastDisplayed) { // Kiểm tra nếu Toast chưa được hiển thị
                    Toast.makeText(getContext(), "Dữ liệu đã tồn tại trong cơ sở dữ liệu", Toast.LENGTH_SHORT).show();
                    toastDisplayed = true; // Đánh dấu rằng Toast đã được hiển thị
                }
            }
        });
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
    private void showWeatherInfo() {
        // Hiển thị thông tin nhiệt độ và UV lên giao diện
        Connection connection = Connection.getInstance(getContext());
        ThoiTietDAO db = connection.thoiTietDAO();
        ThoiTiet thoiTiet1 = db.getLastThoiTiet();
        if(thoiTiet1!=null){
            nhietDo.setText(String.valueOf(thoiTiet1.getNhietDo()) + "°C");
            if(Float.parseFloat(thoiTiet1.getUV())>=0.0 && Float.parseFloat(thoiTiet1.getUV())<=2.0){
                tiaUv.setText("Chi so UV: Thấp");
                khuyenKhich.setText("Khuyến khích vận động");
            }
            else if(Float.parseFloat(thoiTiet1.getUV())>2.0 && Float.parseFloat(thoiTiet1.getUV())<=8.0){
                tiaUv.setText("Chi so UV: Trung Bình");
                khuyenKhich.setText("Khuyến khích vận động trong nhà");
            }
            else if(Float.parseFloat(thoiTiet1.getUV())>8.0 && Float.parseFloat(thoiTiet1.getUV())<=11.0){
                tiaUv.setText("Chi so UV: Cao");
                khuyenKhich.setText("Hạn chế ra đường");
            }
            else {
                tiaUv.setText("Chi so UV: Cực cao");
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
            else if(thoiTiet1.getTyLeMua().contains("Quang") || thoiTiet1.getTyLeMua().contains("quang")){
                imgThoiTiet.setImageResource(R.drawable.quang_dep);
            }
            else if(thoiTiet1.getTyLeMua().contains("Mù") || thoiTiet1.getTyLeMua().contains("mù")){
                imgThoiTiet.setImageResource(R.drawable.co_may);
            }
        }
        else{
            nhietDo.setText("30°C");
            tiaUv.setText("Chi so UV: Thấp");
            khuyenKhich.setText("Khuyến khích vận động");
            viTri.setText("Ha Noi");
            tinhTrang.setText("Nắng");
            imgThoiTiet.setImageResource(R.drawable.anhnangnong);
        }
    }
    private void setSucKhoe(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());

        Connection connection = Connection.getInstance(getContext());
        SucKhoeDAO db = connection.sucKhoeDAO();

        SucKhoe sucKhoe = db.getSucKhoeByDate(today);
        if(sucKhoe!=null){
            long time = Integer.parseInt(sucKhoe.getTime());
            long hours = time / 3600;
            long minutes = (time % 3600) / 60;
            soBuoc.setText(sucKhoe.getBuocChan()+ " bước");

            Kcal.setText((int) Float.parseFloat(sucKhoe.getKcal())+" Kcal");
            ThoiGian.setText(String.valueOf(hours)+ " giờ "+String.valueOf(minutes) + " phút");
            KcalNgay.setText(String.valueOf((int) Float.parseFloat(sucKhoe.getKcal())));
        }
    }
    private void setThoiGian(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String today = dateFormat.format(new Date());

        Connection connection = Connection.getInstance(getContext());
        ThoiGianSuDungDienThoaiDAO db = connection.thoiGianSuDungDienThoaiDAO();

        ThoiGianSuDungDienThoai thoigian = db.ThoiGianSuDungDienThoaiByDate(today);
        if(thoigian!=null){
            long totalSeconds = Integer.parseInt(thoigian.getPhut());
            long hours = totalSeconds / 3600;
            long minutes = (totalSeconds % 3600) / 60;
            if(hours>=8){
                ChuYSucKhoe.setText("Chú ý sức khỏe");
            }
            else{
                ChuYSucKhoe.setText("Bạn đang làm tốt");
            }
            thoigiansddienthoai.setText(String.valueOf(hours) +" giờ "+String.valueOf(minutes)+" phút");
        }
        ThoiGianSuDungDienThoai thoigiantruoc = db.getThoiGianHomQua();
        if(thoigiantruoc!=null){
            long time = Integer.parseInt(thoigiantruoc.getPhut())- Integer.parseInt(thoigian.getPhut());
            if(time<0){
                long time1 = time*-1;
                long hours = time1 / 3600;
                long minutes = (time1 % 3600) / 60;
                NhieuHonHomQua.setText("Nhiều hơn hôm qua "+ String.valueOf(hours) +" giờ "+ String.valueOf(minutes) +" phút");
            }
            else{
                long hours = time / 3600;
                long minutes = (time % 3600) / 60;
                NhieuHonHomQua.setText("Ít hơn hôm qua "+ String.valueOf(hours) +" giờ "+ String.valueOf(minutes) +" phút");
            }
        }
        else if(thoigiantruoc==null){
            NhieuHonHomQua.setText("Không có dữ liệu hôm qua");
        }
//        else{
//            NhieuHonHomQua.setText("Không có dữ liệu hôm qua");
//        }
    }
    private void setMucTieu(){
        Connection connection = Connection.getInstance(getContext());
        BanThanDAO db1 = connection.banThanDAO();
        BanThan banThan = db1.getBanThan();
        if(banThan!=null){
            MucTieu.setText("/"+banThan.getMucTieu()+" Kcal");
            if(KcalNgay.getText() == MucTieu.getText()){
                KcalNgay.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
        else{
            MucTieu.setText("/Chưa xác định mục tiêu");
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        showWeatherInfo();
    }
}
