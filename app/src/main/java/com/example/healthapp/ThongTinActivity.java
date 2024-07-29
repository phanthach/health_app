package com.example.healthapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.healthapp.DAO.BanThanDAO;
import com.example.healthapp.Database.Connection;
import com.example.healthapp.Model.BanThan;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Calendar;

public class ThongTinActivity extends AppCompatActivity{
    private static final int PICK_IMAGE_REQUEST = 1;
    private CardView cardViewGioiTinh, cardViewChieuCao, cardViewCanNang;
    private TextView gioiTinh;
    private TextView chieuCao;
    private TextView canNang;
    private TextView sinhNhat;
    private RadioGroup radioGroup;

    private NumberPicker numberPickerKG,numberPickerG, numberPickerChieuCao1, numberPickerChieuCao2;

    private TextView huyGioiTinh, luuGioiTinh;
    private TextView huyChieuCao, luuChieuCao;
    private TextView huyCanNang, luuCanNang;
    private TextView boSuuTap, Thoat, Luu;
    private EditText hoVaTen;
    private ImageView Avatar;

    private ImageView Muc1, Muc2, Muc3, Muc4;
    private int checkmucdo = 0;
    private TextView mot, hai, ba, bon, mucdo, giaithichmucdo,mucdovandong, giaithich, BMI, BMR, coThe, mucTieu;

    private int nam, thang, ngay, gio, phut;
    BanThan banThan = new BanThan();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin);
        init();
        onClickLis();
        loadDb();
    }

    private void loadDb() {
        Connection connection = Connection.getInstance(getApplicationContext());
        BanThanDAO db = connection.banThanDAO();
        BanThan banThan = db.getBanThan();
        if(banThan!=null){
            hoVaTen.setText(banThan.getHoVaTen());
            gioiTinh.setText(banThan.getGioiTinh());
            chieuCao.setText(banThan.getChieuCao());
            canNang.setText(banThan.getCanNang());
            sinhNhat.setText(banThan.getNgaySinh());
            byte[] imageData = banThan.getAvatar();
            if (imageData != null) {
                // Chuyển đổi dữ liệu hình ảnh từ mảng byte thành một Bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

                // Đặt Bitmap vào ImageView
                Avatar.setImageBitmap(bitmap);
            }
            else{
                if(banThan.getGioiTinh().equals("Nam")){
                    Avatar.setImageResource(R.drawable.nam);
                }
                else {
                    Avatar.setImageResource(R.drawable.nu);
                }
            }
            if(banThan.getMucDoVanDong().equals("1")){
                mot.setText("1");
                checkmucdo=1;
            }
            else if(banThan.getMucDoVanDong().equals("2")){
                hai.setText("2");
                checkmucdo=2;
            }
            else if(banThan.getMucDoVanDong().equals("3")){
                ba.setText("3");
                checkmucdo=3;
            }
            else {
                bon.setText("4");
                checkmucdo=4;
            }
        }
    }

    private void saveDatabase() {
        float BMI, BMR;
        long chiso = 0;

        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        DecimalFormat decimalFormat = new DecimalFormat("#.#");

        Connection connection = Connection.getInstance(getApplicationContext());
        BanThanDAO db = connection.banThanDAO();
        BMI = (float) Float.parseFloat(canNang.getText().toString())/(Float.parseFloat(chieuCao.getText().toString())*Float.parseFloat(chieuCao.getText().toString()))*10000;
        if(gioiTinh.getText()=="Nam"){

            String a = sinhNhat.getText().toString();
            String[] sn = a.split("/");
            int snn = currentYear - Integer.parseInt(sn[2]);
            BMR = 66 + ((float) 13.7*Float.parseFloat(canNang.getText().toString())) + ((float) 5*Float.parseFloat(chieuCao.getText().toString()))-((float) 6.8 * snn );
        } else{
            String a = (String) sinhNhat.getText();
            String[] sn = a.split("/");
            int snn = currentYear - Integer.parseInt(sn[2]);
            BMR = 655 + ((float) 9.6*Float.parseFloat(canNang.getText().toString())) + ((float) 1.5*Float.parseFloat(chieuCao.getText().toString()))-((float) 4.7 * snn );
        }
        banThan.setHoVaTen(hoVaTen.getText().toString());
        banThan.setGioiTinh(gioiTinh.getText().toString());
        banThan.setChieuCao(chieuCao.getText().toString());
        banThan.setCanNang(canNang.getText().toString());
        banThan.setNgaySinh(sinhNhat.getText().toString());
        banThan.setMucDoVanDong(String.valueOf(checkmucdo));
        if(checkmucdo==1){
            chiso = (long) (BMR*1.2);
        }
        else if(checkmucdo==2){
            chiso = (long) (BMR*1.375);
        }
        if(checkmucdo==3){
            chiso = (long) (BMR*1.725);
        }
        if(checkmucdo==4){
            chiso = (long) (BMR*1.9);
        }
        banThan.setMucTieu(String.valueOf(chiso));
        String BMII = decimalFormat.format(BMI).replace(",", ".");
        banThan.setBMI(BMII);
        banThan.setBMR(String.valueOf(BMR));
        db.insertThongTin(banThan);
    }

    private void init(){
        giaithich = findViewById(R.id.giaithich);
        mucdovandong = findViewById(R.id.mucdovandong);
        Muc1 = findViewById(R.id.muc1);
        Muc2 = findViewById(R.id.muc2);
        Muc3 = findViewById(R.id.muc3);
        Muc4 = findViewById(R.id.muc4);
        mot = findViewById(R.id.khonghoatdong);
        hai = findViewById(R.id.vanDongNhe);
        ba =findViewById(R.id.vanDongVua);
        bon = findViewById(R.id.vanDongNangDong);
        BMI = findViewById(R.id.txtBMI);
        BMR=findViewById(R.id.txtBMR);
        coThe = findViewById(R.id.txtNhacNho);
        mucTieu = findViewById(R.id.txtMucTieu);

        Thoat = findViewById(R.id.thoat);
        Luu = findViewById(R.id.luu);

        Avatar=findViewById(R.id.avatar);
        boSuuTap = findViewById(R.id.boSuuTap);

        boSuuTap.setOnClickListener(v -> openGallery());

        cardViewGioiTinh = findViewById(R.id.setGioiTinh);
        cardViewChieuCao = findViewById(R.id.setChieuCao);
        cardViewCanNang = findViewById(R.id.setCanNang);

        hoVaTen = findViewById(R.id.editHoTen);
        gioiTinh = findViewById(R.id.txtGioiTinh);
        chieuCao = findViewById(R.id.txtChieuCao);
        canNang = findViewById(R.id.txtCanNang);
        sinhNhat = findViewById(R.id.txtSinhNhat);

        radioGroup = findViewById(R.id.rGroup);
        huyGioiTinh = findViewById(R.id.huyGioiTinh);
        luuGioiTinh = findViewById(R.id.luuGioiTinh);

        luuCanNang = findViewById(R.id.luuCanNang);
        huyCanNang = findViewById(R.id.huyCanNang);
        numberPickerKG = findViewById(R.id.numberPickerKG);
        numberPickerKG.setMinValue(10);
        numberPickerKG.setMaxValue(300);

        numberPickerG = findViewById(R.id.numberPickerG);
        numberPickerG.setMinValue(0);
        numberPickerG.setMaxValue(10 );

        huyChieuCao = findViewById(R.id.huyChieuCao);
        luuChieuCao = findViewById(R.id.luuChieuCao);

        numberPickerChieuCao1 = findViewById(R.id.numberPicker1);
        numberPickerChieuCao1.setMinValue(60);
        numberPickerChieuCao1.setMaxValue(300);

        numberPickerChieuCao2 = findViewById(R.id.numberPicker2);
        numberPickerChieuCao2.setMinValue(0);
        numberPickerChieuCao2.setMaxValue(9);

    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
    }
    private void onClickLis(){
//        Muc1, Muc2, Muc3, Muc4
//        mot, hai, ba, bon, mucdo, giaithichmucdo
        Muc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mot.setText("1");
                checkmucdo = 1;
                hai.setText("");
                ba.setText("");
                bon.setText("");
                Log.d("================================================",String.valueOf(checkmucdo));
                mucdovandong.setText("Không hoăc ít vận động");
                giaithich.setText("Hoạt động hằng ngày thông thường");
            }
        });
        Muc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mot.setText("");
                hai.setText("2");
                checkmucdo = 2;
                Log.d("================================================",String.valueOf(checkmucdo));
                ba.setText("");
                bon.setText("");
                mucdovandong.setText("Có hoạt động");
                giaithich.setText("Hoạt động hằng ngày và có 30-60 phút vận động thể thao");
            }
        });
        Muc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mot.setText("");
                hai.setText("");
                ba.setText("3");
                checkmucdo = 3;
                Log.d("================================================",String.valueOf(checkmucdo));
                bon.setText("");
                mucdovandong.setText("Năng động");
                giaithich.setText("Hoạt động hằng ngày và có ít nhất 60 phút vận động thể thao");
            }
        });
        Muc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mot.setText("");
                hai.setText("");
                ba.setText("");
                bon.setText("4");
                checkmucdo = 4;
                Log.d("================================================",String.valueOf(checkmucdo));
                mucdovandong.setText("Rất năng động");
                giaithich.setText("Hoạt động hằng ngày và vận động thể theo cường độ cao");
            }
        });

        Thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        hoVaTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoVaTen.setCursorVisible(true);
            }
        });
        gioiTinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewGioiTinh.setVisibility(view.VISIBLE);
                cardViewCanNang.setVisibility(view.GONE);
                cardViewChieuCao.setVisibility(View.GONE);

                huyGioiTinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cardViewGioiTinh.setVisibility(view.GONE);
                    }
                });
                luuGioiTinh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(radioGroup.getCheckedRadioButtonId()==R.id.gtNam){
                            gioiTinh.setText("Nam");
                            Avatar.setImageResource(R.drawable.nam);
                        }
                        else if(radioGroup.getCheckedRadioButtonId()==R.id.gtNu){
                            gioiTinh.setText("Nữ");
                            Avatar.setImageResource(R.drawable.nu);
                        }
                        cardViewGioiTinh.setVisibility(view.GONE);
                    }
                });
            }
        });
        chieuCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewChieuCao.setVisibility(view.VISIBLE);
                cardViewGioiTinh.setVisibility(view.GONE);
                cardViewCanNang.setVisibility(view.GONE);

                huyChieuCao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cardViewChieuCao.setVisibility(view.GONE);
                    }
                });
                luuChieuCao.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int chieuCao1 = numberPickerChieuCao1.getValue();
                        int chieuCao2 = numberPickerChieuCao2.getValue();

                        chieuCao.setText(String.valueOf(chieuCao1) + "." + String.valueOf(chieuCao2));
                        cardViewChieuCao.setVisibility(View.GONE);
                    }
                });
            }
        });
        canNang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cardViewCanNang.setVisibility(view.VISIBLE);
                cardViewGioiTinh.setVisibility(view.GONE);
                cardViewChieuCao.setVisibility(View.GONE);

                huyCanNang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cardViewCanNang.setVisibility(view.GONE);
                    }
                });
                luuCanNang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int CanNangKg = numberPickerKG.getValue();
                        int CanNangG = numberPickerG.getValue();
                        canNang.setText(String.valueOf(CanNangKg) +"."+String.valueOf(CanNangG));
                        cardViewCanNang.setVisibility(view.GONE);
                    }
                });
            }
        });
        sinhNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(ThongTinActivity.this);
            }
        });
        Thoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hoVaTen.getText().toString()=="" || gioiTinh.getText().toString()=="" || chieuCao.getText().toString()=="" || canNang.getText().toString()=="" || sinhNhat.getText().toString()==""||checkmucdo==0){
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                }
                else{
                    saveDatabase();
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("checkValue", true);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });
    }
    private void showDatePickerDialog(Context context) {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Xử lý khi người dùng chọn ngày
                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                        sinhNhat.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Lấy URI của ảnh được chọn
            Uri imageUri = data.getData();

            // Sử dụng ContentResolver để mở InputStream từ URI
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                // Sử dụng BitmapFactory để tạo Bitmap từ InputStream
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                Bitmap roundedBitmap = getRoundedBitmap(bitmap);
                // Chuyển đổi Bitmap thành mảng byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                roundedBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                banThan.setAvatar(byteArray);
                
                // Hiển thị Bitmap trong ImageView
                Avatar.setImageBitmap(roundedBitmap );

                // Đóng InputStream sau khi đã sử dụng xong
                inputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        // Lấy kích thước của ảnh bitmap
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        // Tính toán kích thước mới cho ảnh sao cho tỷ lệ là 1:1 (120:120)
        int newSize = Math.min(width, height);
        int x = (width - newSize) / 2;
        int y = (height - newSize) / 2;

        // Tạo bitmap mới với kích thước mới và cắt từ vị trí trung tâm
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, x, y, newSize, newSize);

        // Tạo bitmap tròn từ bitmap mới đã cắt
        Bitmap output = Bitmap.createBitmap(newSize, newSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final int radius = newSize / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        canvas.drawCircle(radius, radius, radius, paint);

        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(croppedBitmap, 0, 0, paint);

        return output;
    }

}