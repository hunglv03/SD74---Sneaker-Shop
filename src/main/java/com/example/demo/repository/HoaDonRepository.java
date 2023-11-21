package com.example.demo.repository;

import com.example.demo.model.HoaDon;
import com.example.demo.model.KhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon , UUID> {
    @Query(value = "select * from Hoa_Don where trang_thai = 3 ",nativeQuery = true)
    List<HoaDon> listChuaThanhToan();
//    @Query(value = "select hd  from HoaDon hd order by hd.tgTao desc")
//    @Query(value = "select * from hoa_don order by tg_tao desc",nativeQuery = true)
    List<HoaDon> findAllByOrderByTgTaoDesc();

    List<HoaDon> findByKhachHangAndLoaiHD(KhachHang khachHang, int loaiHD);

    @Query(value = "select COUNT(id_hd)\n" +
            "from hoa_don where trang_thai =3 or trang_thai=4",nativeQuery = true)
    Integer getAllHoaDonCho();

    @Query(value = "select COUNT(id_hd)\n" +
            "from hoa_don where trang_thai =1 or trang_thai=2",nativeQuery = true)
    Integer getAllHoaDonDaThanhToan();

    List<HoaDon> findByKhachHangAndLoaiHDAndTrangThaiOrTrangThaiOrTrangThai(KhachHang khachHang,int loaiHD, int trangThai1, int trangThai2, int trangThai3);

}
