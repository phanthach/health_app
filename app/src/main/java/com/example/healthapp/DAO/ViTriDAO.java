package com.example.healthapp.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ViTriDAO {
    @Query("INSERT INTO ViTri (TenTinh, vitri) VALUES " +
            "('Hà Nội', 'Ha Noi'), " +
            "('Hồ Chí Minh', 'Ho Chi Minh'), " +
            "('Hải Phòng', 'Hai Phong'), " +
            "('Đà Nẵng', 'Da Nang'), " +
            "('Cần Thơ', 'Can Tho'), " +
            "('Hải Dương', 'Hai Duong'), " +
            "('Bắc Ninh', 'Bac Ninh'), " +
            "('Hà Nam', 'Ha Nam'), " +
            "('Hưng Yên', 'Hung Yen'), " +
            "('Hòa Bình', 'Hoa Binh'), " +
            "('Hà Giang', 'Ha Giang'), " +
            "('Tuyên Quang', 'Tuyen Quang'), " +
            "('Lào Cai', 'Lao Cai'), " +
            "('Lai Châu', 'Lai Chau'), " +
            "('Sơn La', 'Son La'), " +
            "('Yên Bái', 'Yen Bai'), " +
            "('Lạng Sơn', 'Lang Son'), " +
            "('Thái Nguyên', 'Thai Nguyen'), " +
            "('Phú Thọ', 'Phu Tho'), " +
            "('Bắc Giang', 'Bac Giang'), " +
            "('Quảng Ninh', 'Quang Ninh'), " +
            "('Thái Bình', 'Thai Binh'), " +
            "('Nam Định', 'Nam Dinh'), " +
            "('Ninh Bình', 'Ninh Binh'), " +
            "('Thanh Hóa', 'Thanh Hoa'), " +
            "('Nghệ An', 'Nghe An'), " +
            "('Hà Tĩnh', 'Ha Tinh'), " +
            "('Quảng Bình', 'Quang Binh'), " +
            "('Quảng Trị', 'Quang Tri'), " +
            "('Thừa Thiên Huế', 'Thua Thien Hue'), " +
            "('Quảng Nam', 'Quang Nam'), " +
            "('Quảng Ngãi', 'Quang Ngai'), " +
            "('Bình Định', 'Binh Dinh'), " +
            "('Phú Yên', 'Phu Yen'), " +
            "('Khánh Hòa', 'Khanh Hoa'), " +
            "('Ninh Thuận', 'Ninh Thuan'), " +
            "('Bình Thuận', 'Binh Thuan'), " +
            "('Kon Tum', 'Kon Tum'), " +
            "('Gia Lai', 'Gia Lai'), " +
            "('Đắk Lắk', 'Dak Lak'), " +
            "('Đắk Nông', 'Dak Nong'), " +
            "('Lâm Đồng', 'Lam Dong'), " +
            "('Bình Phước', 'Binh Phuoc'), " +
            "('Tây Ninh', 'Tay Ninh'), " +
            "('Bình Dương', 'Binh Duong'), " +
            "('Đồng Nai', 'Dong Nai'), " +
            "('Bà Rịa-Vũng Tàu', 'Ba Ria-Vung Tau'), " +
            "('Long An', 'Long An'), " +
            "('Tiền Giang', 'Tien Giang'), " +
            "('Bến Tre', 'Ben Tre'), " +
            "('Trà Vinh', 'Tra Vinh'), " +
            "('Vĩnh Long', 'Vinh Long'), " +
            "('Đồng Tháp', 'Dong Thap'), " +
            "('An Giang', 'An Giang'), " +
            "('Kiên Giang', 'Kien Giang'), " +
            "('Cần Thơ', 'Can Tho'), " +
            "('Hậu Giang', 'Hau Giang'), " +
            "('Sóc Trăng', 'Soc Trang'), " +
            "('Bạc Liêu', 'Bac Lieu'), " +
            "('Cà Mau', 'Ca Mau'), " +
            "('Điện Biên', 'Dien Bien'), " +
            "('Bắc Kạn', 'Bac Kan'), " +
            "('Sơn La', 'Son La')")
    void insetViTri();

    @Query("SELECT TenTinh FROM ViTri ")
    List<String> getAllTenTinh();

    @Query("SELECT vitri FROM ViTri WHERE TenTinh = :tenTinh")
    String getViTri(String tenTinh);


}
