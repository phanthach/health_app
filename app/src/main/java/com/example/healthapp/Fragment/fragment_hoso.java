package com.example.healthapp.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.healthapp.CustomDatePickerDialog;
import com.example.healthapp.DAO.BanThanDAO;
import com.example.healthapp.DAO.SucKhoeDAO;
import com.example.healthapp.DAO.ThoiGianSuDungDienThoaiDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.BanThan;
import com.example.healthapp.Model.SucKhoe;
import com.example.healthapp.Model.ThoiGianSuDungDienThoai;
import com.example.healthapp.R;
import com.example.healthapp.ThongTinActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class fragment_hoso extends Fragment {
    private TextView btSua, hoVaTen, capNhatThongTin, Sua, BMI, BMR, nhacNho, mucTieu, dibo, kcal, thoigiansddt, ngaydibo, ngaykcal, ngaysddt;
    private ImageView imageAvatar;
    private TextView Tu, den, thoigianvd, thoigiansd;
    private static final int YOUR_REQUEST_CODE = 1; // Đặt mã request code của bạn ở đây

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoso, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btSua = view.findViewById(R.id.btSua);
        hoVaTen =view.findViewById(R.id.hoVaTen);
        capNhatThongTin = view.findViewById(R.id.capNhatThongTin);
        imageAvatar = view.findViewById(R.id.imageAvatar);
        BMI = view.findViewById(R.id.txtBMI);
        BMR = view.findViewById(R.id.txtBMR);
        nhacNho = view.findViewById(R.id.txtNhacNho);
        mucTieu = view.findViewById(R.id.txtMucTieu);
        dibo = view.findViewById(R.id.txtBuocChan);
        ngaydibo = view.findViewById(R.id.txtNgayBuocChan);
        kcal = view.findViewById(R.id.txtKcal);
        ngaykcal = view.findViewById(R.id.txtNgayKcal);
        thoigiansddt = view.findViewById(R.id.txtSDDT);
        ngaysddt = view.findViewById(R.id.txtNgayDungDienThoai);
        Tu = view.findViewById(R.id.txtTime1);
        den = view.findViewById(R.id.txtTime2);
        thoigianvd = view.findViewById(R.id.txtThoiGianVanDongTB);
        thoigiansd = view.findViewById(R.id.txtThoiGianDungDienThoaiTB);

        capNhatThongTin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThongTinActivity.class);
                startActivityForResult(intent, YOUR_REQUEST_CODE);
            }
        });
        btSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThongTinActivity.class);
                startActivityForResult(intent, YOUR_REQUEST_CODE);
            }
        });
        Tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomDatePickerDialog();
            }
        });

        setNgayHienTai();
        setThanhTich();

        Connection connection = Connection.getInstance(getContext());
        BanThanDAO db = connection.banThanDAO();
        BanThan banThan = db.getBanThan();
        if(banThan != null){
            hoVaTen.setText(banThan.getHoVaTen());
            BMI.setText("Chỉ số BMI: " + banThan.getBMI());
            BMR.setText("Chỉ số BMR: " + banThan.getBMR());
            if((float) Float.parseFloat(banThan.getBMI())<18.5){
                nhacNho.setText("Bạn đang rất gầy, nên chú ý ăn uống và rèn luyện sức khỏe");
            }
            else if((float) Float.parseFloat(banThan.getBMI())>=18.5 && (float) Float.parseFloat(banThan.getBMI())<24.9){
                nhacNho.setText("Bạn đang ở thể trạng tốt, hãy duy trì nó.");
            }
            else if((float) Float.parseFloat(banThan.getBMI())>=24.9 && (float) Float.parseFloat(banThan.getBMI())<30){
                nhacNho.setText("Bạn đang bị thừa cân, hãy lên kế hoạch giảm cân khoa học");
            }
            else if((float) Float.parseFloat(banThan.getBMI())>=30.0 && (float) Float.parseFloat(banThan.getBMI())<35.0){
                nhacNho.setText("Bạn đang bị béo phì cấp độ 1, hãy hành động ngay hôm nay");
            }
            else if((float) Float.parseFloat(banThan.getBMI())>=35.0 && (float) Float.parseFloat(banThan.getBMI())<40){
                nhacNho.setText("Bạn đang bị béo phì cấp độ 2, trạng thái cơ thể bạn khá nghiêm trọng hãy thăm khám và nhận liệu trình từ bác sĩ");
            }
            else if((float) Float.parseFloat(banThan.getBMI())>40){
                nhacNho.setText("Bạn đang bị béo phì cấp độ 3, cơ thể bạn đang ở mức độ béo phì nguy hiểm, hãy thăm khám và lên liệu trình ngay");
            }
            mucTieu.setText("Với mức độ hoạt động mà bạn cung cấp và chỉ số BMR của bạn\nMức tiêu thụ Kcal một ngày của bạn là: " +banThan.getMucTieu());
            byte[] imageData = banThan.getAvatar();
            if (imageData != null) {
                // Chuyển đổi dữ liệu hình ảnh từ mảng byte thành một Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Đặt Bitmap vào ImageView
                imageAvatar.setImageBitmap(bitmap);
            }
            capNhatThongTin.setVisibility(View.GONE);
            btSua.setVisibility(View.VISIBLE);

        }
        else{
            capNhatThongTin.setVisibility(View.VISIBLE);
            btSua.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == YOUR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    boolean checkValue = data.getBooleanExtra("checkValue", true);
                    if (checkValue) {
                        Connection connection = Connection.getInstance(getContext());
                        BanThanDAO db = connection.banThanDAO();
                        BanThan banThan = db.getBanThan();
                        hoVaTen.setText(banThan.getHoVaTen());
                        capNhatThongTin.setVisibility(View.GONE);
                        BMI.setText("Chỉ số BMI: " + banThan.getBMI());
                        BMR.setText("Chỉ số BMR: " + banThan.getBMR());
                        if((float) Float.parseFloat(banThan.getBMI())<18.5){
                            nhacNho.setText("Bạn đang rất gầy, nên chú ý ăn uống và rèn luyện sức khỏe");
                        }
                        else if((float) Float.parseFloat(banThan.getBMI())>=18.5 && (float) Float.parseFloat(banThan.getBMI())<24.9){
                            nhacNho.setText("Bạn đang ở thể trạng tốt, hãy duy trì nó.");
                        }
                        else if((float) Float.parseFloat(banThan.getBMI())>=24.9 && (float) Float.parseFloat(banThan.getBMI())<30){
                            nhacNho.setText("Bạn đang bị thừa cân, hãy lên kế hoạch giảm cân khoa học");
                        }
                        else if((float) Float.parseFloat(banThan.getBMI())>=30.0 && (float) Float.parseFloat(banThan.getBMI())<35.0){
                            nhacNho.setText("Bạn đang bị béo phì cấp độ 1, hãy hành động ngay hôm nay");
                        }
                        else if((float) Float.parseFloat(banThan.getBMI())>=35.0 && (float) Float.parseFloat(banThan.getBMI())<40){
                            nhacNho.setText("Bạn đang bị béo phì cấp độ 2, trạng thái cơ thể bạn khá nghiêm trọng hãy thăm khám và nhận liệu trình từ bác sĩ");
                        }
                        else if((float) Float.parseFloat(banThan.getBMI())>40){
                            nhacNho.setText("Bạn đang bị béo phì cấp độ 3, cơ thể bạn đang ở mức độ béo phì nguy hiểm, hãy thăm khám và lên liệu trình ngay");
                        }
                        mucTieu.setText("Với mức độ hoạt động mà bạn cung cấp và chỉ số BMR của bạn\nMức tiêu thụ Kcal một ngày của bạn là: " +banThan.getMucTieu()+" Kcal");
                        btSua.setVisibility(View.VISIBLE);
                        byte[] imageData = banThan.getAvatar();
                        if (imageData != null) {
                            // Chuyển đổi dữ liệu hình ảnh từ mảng byte thành một Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                            // Đặt Bitmap vào ImageView
                            imageAvatar.setImageBitmap(bitmap);
                        }
                    }
                }
            }
        }
    }
    private void setThanhTich(){
        Connection connection = Connection.getInstance(getContext());
        SucKhoeDAO db = connection.sucKhoeDAO();
        SucKhoe sucKhoe = db.thanhTinhBuocChan();
        if(sucKhoe!=null){
            dibo.setText(sucKhoe.getBuocChan());
            String[] diboo = sucKhoe.getNgay().split("-");
            ngaydibo.setText(diboo[2]+"/"+diboo[1]);

        }

        SucKhoe sucKhoe1 = db.thanhTinhKcal();
        if(sucKhoe1!=null){
            kcal.setText(String.valueOf((int) Float.parseFloat(sucKhoe1.getKcal())));
            String[] kcal = sucKhoe1.getNgay().split("-");
            ngaykcal.setText(kcal[2]+"/"+kcal[1]);
        }
        ThoiGianSuDungDienThoaiDAO db1 = connection.thoiGianSuDungDienThoaiDAO();
        ThoiGianSuDungDienThoai thoiGianSuDungDienThoai = db1.thanhTinhSDDT();
        if(thoiGianSuDungDienThoai!=null){
            long time = Integer.parseInt(thoiGianSuDungDienThoai.getPhut());
            long hours = time / 3600;
            long minutes = (time % 3600) / 60;
            thoigiansddt.setText(hours+" giờ "+minutes+" phút");
            String[] ngaysddtt = thoiGianSuDungDienThoai.getNgay().split("-");
            ngaysddt.setText(ngaysddtt[2]+"/"+ngaysddtt[1]);
        }

    }
    private void showCustomDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        CustomDatePickerDialog datePickerDialog = new CustomDatePickerDialog(getContext(),
                (view, year1, month1, dayOfMonth) -> {
                    calendar.set(year1, month1, dayOfMonth);
                    Tu.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);

                    // Tính toán ngày về sau 7 ngày
                    calendar.add(Calendar.DAY_OF_MONTH, 7);
                    int returnYear = calendar.get(Calendar.YEAR);
                    int returnMonth = calendar.get(Calendar.MONTH);
                    int returnDay = calendar.get(Calendar.DAY_OF_MONTH)-1;
                    den.setText(returnDay + "/" + (returnMonth + 1) + "/" + returnYear);
                    Connection connection = Connection.getInstance(getContext());
                    ThoiGianSuDungDienThoaiDAO  db = connection.thoiGianSuDungDienThoaiDAO();
                    String[] tuu = Tu.getText().toString().split("/");
                    String toNgay = tuu[2]+"-"+tuu[1]+"-"+tuu[0];
                    String[] denn = den.getText().toString().split("/");
                    String fromNgay = denn[2]+"-"+denn[1]+"-"+denn[0];
                    String timee =String.valueOf(db.getSuDungDienThoai(toNgay, fromNgay));
                    if(timee!=null){
                        long time = Integer.parseInt(timee);
                        long hours = time / 3600;
                        long minutes = (time % 3600) / 60;

                        thoigiansd.setText(hours +" giờ " + minutes+" phút");
                    }
                    else{
                        thoigiansd.setText("0 giờ 0 phút");
                    }
                    SucKhoeDAO db1 = connection.sucKhoeDAO();
                    String timee1 =String.valueOf(db1.getVanDong(toNgay, fromNgay));
                    if(timee1!=null){
                        long time1 = (long) Float.parseFloat(timee1);
                        long hours1 = time1 / 3600;
                        long minutes1 = (time1 % 3600) / 60;

                        thoigianvd.setText(hours1 +" giờ " + minutes1+" phút");
                    }
                    else{
                        thoigianvd.setText("0 giờ 0 phút");
                    }
                }, year, month, day);

        // Đặt ngày tối đa là ngày hiện tại
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
    private void setNgayHienTai(){
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        // Định dạng ngày tháng
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Hiển thị ngày thứ 2 gần nhất vào TextView
        Tu.setText(sdf1.format(calendar.getTime()));
        // Định dạng ngày tháng
        // Hiển thị ngày hiện tại vào TextView
        den.setText(sdf1.format(calendar1.getTime()));

        Connection connection = Connection.getInstance(getContext());
        ThoiGianSuDungDienThoaiDAO  db = connection.thoiGianSuDungDienThoaiDAO();
        SucKhoeDAO db1 = connection.sucKhoeDAO();
        String[] tuu = Tu.getText().toString().split("/");
        String toNgay = tuu[2]+"-"+tuu[1]+"-"+tuu[0];
        String[] denn = den.getText().toString().split("/");
        String fromNgay = denn[2]+"-"+denn[1]+"-"+denn[0];
        String timee = String.valueOf(db.getSuDungDienThoai(toNgay, fromNgay));

        if(timee!=null){
            long time =(long) Float.parseFloat(timee);
            long hours = time / 3600;
            long minutes = (time % 3600) / 60;
            thoigiansd.setText(String.valueOf(hours) +" giờ " + String.valueOf(minutes)+" phút");
        }
        else{
            thoigiansd.setText("0 giờ 0 phút");
        }

        String timee1 =String.valueOf(db1.getVanDong(toNgay, fromNgay));
        if(timee1!=null){
            long time1 =(long) Float.parseFloat(timee1);
            long hours1 = time1 / 3600;
            long minutes1 = (time1 % 3600) / 60;

            thoigianvd.setText(String.valueOf(hours1) +" giờ " + String.valueOf(minutes1)+" phút");
        }
        else {
            thoigianvd.setText("0 giờ 0 phút");
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        setThanhTich();
    }
}

