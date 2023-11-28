package com.example.demo.controller;

import com.example.demo.repository.*;

import com.example.demo.service.CTGViewModelService;
import com.example.demo.viewModel.CTGViewModel;
import com.example.demo.viewModel.CTHDViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("manage")
public class ThongKeController {
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private GiayChiTietRepository giayChiTietRepository;
    @Autowired
    private HoaDonRepository hoaDonRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private HinhAnhRepository hinhAnhRepository;
    @Autowired
    private GiayRepository giayRepository;
    @Autowired
    private CTGViewModelService ctgViewModelService;
    @GetMapping("/thongke")
    private String getTong(Model model){
        model.addAttribute("tong",khachHangRepository.getTongKH());
        model.addAttribute("tonggiay",giayChiTietRepository.getTongGiay());

        //làm tròn doanh thu
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        Optional<Double> ltn = hoaDonChiTietRepository.getLaiThangNay();

        Optional<Double> tlbr = hoaDonChiTietRepository.getTongTienLaiCuaHang();
        Optional<Integer> tongSPBanTrongNgay = hoaDonChiTietRepository.getTongSPBanTrongNgay();
        Optional<Integer> tongSPBanTrongThang = hoaDonChiTietRepository.getTongSPBanTrongThang();

        System.out.println(tlbr.get());
        if(tongSPBanTrongNgay.isPresent() ){
            Integer a = tongSPBanTrongNgay.get();

            model.addAttribute("sptn",a);
        }else{
            model.addAttribute("sptn","0");
        }

        if(ltn.isPresent()){
            Double b = ltn.get();
            String formatdtt = currencyFormat.format(b);
            model.addAttribute("ltn",formatdtt);
        }else{
            model.addAttribute("ltn","0đ");
        }
        //Tổng tiền của cửa hàng
        if(tlbr.isPresent()){
            Double c = tlbr.get();
            String formatdtt2 = currencyFormat.format(c);
            model.addAttribute("tlbr",formatdtt2);
        }else{
            model.addAttribute("tlbr","0");
        }

        if(tongSPBanTrongThang.isPresent()){
            Integer d = tongSPBanTrongThang.get();

            model.addAttribute("sptt",d);
        }else{
            model.addAttribute("sptt","0");
        }

//
//        // top 5 sp bán chạy
        List<CTGViewModel> listCTGModelBestSeller = ctgViewModelService.getAllOrderBestSeller();
        List<CTGViewModel> top5CTGModelBestSeller = listCTGModelBestSeller.subList(0, Math.min(listCTGModelBestSeller.size(), 5));

        model.addAttribute("spBanChay",top5CTGModelBestSeller);

//        //chỉ số ở hóa đơn
        model.addAttribute("hdht",hoaDonRepository.getAllHoaDonDaThanhToan());
        model.addAttribute("hdc",hoaDonRepository.getAllHoaDonCho());
//  hoa don
//        model.addAttribute("hoaDonCT2",hoaDonChiTietRepository.getAllHoaDonChoThanhToan());
//        model.addAttribute("hoaDonCT1",hoaDonChiTietRepository.getAllHoaDonDaThanhToan());
        // biểu đồ thống kê
        //theo tháng
        List<String> listThem= Arrays.asList("Tháng 1","Tháng 2","Tháng 3","Tháng 4","Tháng 5",
                "Tháng 6","Tháng 7","Tháng 8","Tháng 9","Tháng 10","Tháng 11","Tháng 12");
        List<String> listThang = new ArrayList<>(listThem);

        List<Integer> list = Arrays.asList(hoaDonChiTietRepository.getThang1(),
                hoaDonChiTietRepository.getThang2(),
                hoaDonChiTietRepository.getThang3(),
                hoaDonChiTietRepository.getThang4(),
                hoaDonChiTietRepository.getThang5(),
                hoaDonChiTietRepository.getThang6(),
                hoaDonChiTietRepository.getThang7(),
                hoaDonChiTietRepository.getThang8(),
                hoaDonChiTietRepository.getThang9(),
                hoaDonChiTietRepository.getThang10(),
                hoaDonChiTietRepository.getThang11(),
                hoaDonChiTietRepository.getThang12()

                );
        List<Integer> listDoanhSo = new ArrayList<>(list);
        model.addAttribute("listThang", listThang);
        model.addAttribute("listDoanhSo", listDoanhSo);
// theo ngày
    List<String> listThemNgay = new ArrayList<>();
    listThemNgay.add("Ngày Hôm Nay");
    List<Integer> doanhSoNgay = new ArrayList<>();
    doanhSoNgay.add(hoaDonChiTietRepository.getNgaythu1());
        model.addAttribute("Ngay", listThemNgay);
        model.addAttribute("listSL", doanhSoNgay);
        //theo năm
        List<String> listThemNam = new ArrayList<>();
        listThemNam.add("2021");
        listThemNam.add("2022");
        listThemNam.add("2023");

        List<Integer> doanhSoNam = new ArrayList<>();
        doanhSoNam.add(hoaDonChiTietRepository.Nam2021());
        doanhSoNam.add(hoaDonChiTietRepository.Nam2022());
        doanhSoNam.add(hoaDonChiTietRepository.Nam2023());
        model.addAttribute("Nam", listThemNam);
        model.addAttribute("listNam", doanhSoNam);
        // chi tiết sp đã bán trong ngày
        List<Object[]> k = hoaDonChiTietRepository.findHoaDonChiTietByDate();
        List<CTHDViewModel> yourDTOList = k.stream()
                .map(result -> new CTHDViewModel(
                        (Double) result[0],
                        (String) result[1],
                        (Double) result[2],
                        (Integer) result[3],
                        (String) result[4],
                        (String) result[5],
                        (String) result[6],
                        (String) result[7]
                )).collect(Collectors.toList());
        // chi tiết sp đã bán trong tháng
        List<Object[]> x = hoaDonChiTietRepository.findHoaDonChiTietByMonth();
        List<CTHDViewModel> yourDTOList2 = x.stream()
                .map(result -> new CTHDViewModel(
                        (Double) result[0],
                        (String) result[1],
                        (Double) result[2],
                        (Integer) result[3],
                        (String) result[4],
                        (String) result[5],
                        (String) result[6],
                        (String) result[7]
                )).collect(Collectors.toList());
            model.addAttribute("getN",yourDTOList);
            model.addAttribute("getT",yourDTOList2);
        return "manage/ThongKe/index";
    }








//    @GetMapping("/hoadoncho/filter")
//    public String searchHoaDonCho(Model model, @RequestParam(name = "searchTerm") String searchTerm) {
//        List<HoaDon> filteredHoaDonCho;
//        if ("Giày".equals(searchTerm) && "Size".equals(searchTerm) && "Màu Sắc".equals(searchTerm)) {
//
//            filteredHoaDonCho = hoaDonCho.getAll();
//        } else {
//
//            filteredHoaDonCho = hoaDonCho.fillterHoaDonCho(searchTerm);
//        }
//        model.addAttribute("HoaDon", filteredHoaDonCho);
//        model.addAttribute("HoaDonAll", hoaDonCho.getAll());
//        return "manage/giay";
//    }
}
