package com.example.demo.controller;

import com.example.demo.config.ExcelExporterGiays;
import com.example.demo.config.ExcelExporterSize;
import com.example.demo.config.PDFExporterGiays;
import com.example.demo.config.PDFExporterSizes;
import com.example.demo.model.*;
import com.example.demo.service.*;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RequestMapping("/manage")
@Controller
public class GiayController {
    @Autowired
    private GiayService giayService;
    @Autowired
    private HangService hangService;
    @Autowired
    private ChatLieuService chatLieuService;
    @Autowired
    private GiayChiTietService giayChiTietService;
    @Autowired
    private HinhAnhService hinhAnhService;
    @Autowired
    private SizeService sizeService;
    @Autowired
    private MauSacService mauSacService;
    @Autowired
    private HttpSession session;

    @ModelAttribute("dsTrangThai")
    public Map<Integer, String> getDsTrangThai() {
        Map<Integer, String> dsTrangThai = new HashMap<>();
        dsTrangThai.put(1, "Hoạt động");
        dsTrangThai.put(0, "Không hoạt động");
        return dsTrangThai;
    }

    @GetMapping("/giay")
    public String dsGiay(Model model, @ModelAttribute("message") String message) {

        List<Giay> giay = giayService.getAllGiay();
        List<Hang> hangs = hangService.getALlHang();
        List<ChatLieu> chatLieus = chatLieuService.getAllChatLieu();
        // Kiểm tra và cập nhật trạng thái của giày nếu trạng thái của hãng hoặc chất liệu không hoạt động (0)
        for (Giay giayItem : giay) {
            if (giayItem.getHang().getTrangThai() == 0 || giayItem.getChatLieu().getTrangThai() == 0) {
                giayItem.setTrangThai(0);
                giayService.save(giayItem);
            }
        }
        Collections.sort(giay, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("giay", giay);
        model.addAttribute("hang", hangs);
        model.addAttribute("chatLieu", chatLieus);
        if (message == null || !"true".equals(message)) {
            model.addAttribute("message", false);
        }
        return "manage/giay";
    }

    @GetMapping("/giay/viewAdd")
    public String viewAddGiay(Model model
            , @ModelAttribute("maGiayError") String maGiayError
            , @ModelAttribute("tenGiayError") String tenGiayError
            , @ModelAttribute("hangError") String hangError
            , @ModelAttribute("chatLieuError") String chatLieuError
            , @ModelAttribute("errorGiay") String errorGiay, @ModelAttribute("userInput") Giay userInputGiay
            , @ModelAttribute("messageHang") String messageHang
            , @ModelAttribute("maHangError") String maHangError
            , @ModelAttribute("tenHangError") String tenHangError
            , @ModelAttribute("errorHang") String errorHang, @ModelAttribute("userInput") Hang userInputHang
            , @ModelAttribute("messageChatLieu") String messageChatLieu
            , @ModelAttribute("maChatLieuError") String maChatLieuError
            , @ModelAttribute("tenChatLieuError") String tenChatLieuError
            , @ModelAttribute("errorChatLieu") String errorChatLieu, @ModelAttribute("userInput") ChatLieu userInputChatLieu) {
        List<Hang> hangList = hangService.getALlHang();
        Collections.sort(hangList, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("hang", hangList);
        //
        List<ChatLieu> chatLieuList = chatLieuService.getAllChatLieu();
        Collections.sort(chatLieuList, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("chatLieu", chatLieuList);
        //
        model.addAttribute("giay", new Giay());
        model.addAttribute("hangAdd", new Hang());
        model.addAttribute("chatLieuAdd", new ChatLieu());
        //
        if (maGiayError == null || !"maGiayError".equals(errorGiay)) {
            model.addAttribute("maGiayError", false);
        }
        if (tenGiayError == null || !"tenGiayError".equals(errorGiay)) {
            model.addAttribute("tenGiayError", false);
        }
        if (hangError == null || !"hangError".equals(errorGiay)) {
            model.addAttribute("hangError", false);
        }
        if (chatLieuError == null || !"chatLieuError".equals(errorGiay)) {
            model.addAttribute("chatLieuError", false);
        }
        // Kiểm tra xem có dữ liệu người dùng đã nhập không và điền lại vào trường nhập liệu
        if (userInputGiay != null) {
            model.addAttribute("giay", userInputGiay);
        }
        //add Hang
        if (messageHang == null || !"true".equals(messageHang)) {
            model.addAttribute("messageHang", false);
        }
        if (maHangError == null || !"maHangError".equals(errorHang)) {
            model.addAttribute("maHangError", false);
        }
        if (tenHangError == null || !"tenHangError".equals(errorHang)) {
            model.addAttribute("tenHangError", false);
        }
        // Kiểm tra xem có dữ liệu người dùng đã nhập không và điền lại vào trường nhập liệu
        if (userInputHang != null) {
            model.addAttribute("hangAdd", userInputHang);
        }
        //add chat-lieu
        if (messageChatLieu == null || !"true".equals(messageChatLieu)) {
            model.addAttribute("messageChatLieu", false);
        }
        if (maChatLieuError == null || !"maChatLieuError".equals(errorChatLieu)) {
            model.addAttribute("maChatLieuError", false);
        }
        if (tenChatLieuError == null || !"tenChatLieuError".equals(errorChatLieu)) {
            model.addAttribute("tenChatLieuError", false);
        }
        // Kiểm tra xem có dữ liệu người dùng đã nhập không và điền lại vào trường nhập liệu
        if (userInputChatLieu != null) {
            model.addAttribute("chatLieuAdd", userInputChatLieu);
        }
        return "manage/add-giay";
    }

    @PostMapping("/giay/viewAdd/add")
    public String addGiay(@Valid @ModelAttribute("giay") Giay giay, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            List<Hang> hangList = hangService.getALlHang();
            Collections.sort(hangList, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
            model.addAttribute("hang", hangList);
            //
            List<ChatLieu> chatLieuList = chatLieuService.getAllChatLieu();
            Collections.sort(chatLieuList, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
            model.addAttribute("chatLieu", chatLieuList);
            //
            model.addAttribute("giay", new Giay());
            model.addAttribute("hangAdd", new Hang());
            model.addAttribute("chatLieuAdd", new ChatLieu());
            if (bindingResult.hasFieldErrors("maGiay")) {
                redirectAttributes.addFlashAttribute("userInput", giay);
                redirectAttributes.addFlashAttribute("errorGiay", "maGiayError");
            }
            if (bindingResult.hasFieldErrors("tenGiay")) {
                redirectAttributes.addFlashAttribute("userInput", giay);
                redirectAttributes.addFlashAttribute("errorGiay", "tenGiayError");
            }
            if (bindingResult.hasFieldErrors("hang")) {
                redirectAttributes.addFlashAttribute("userInput", giay);
                redirectAttributes.addFlashAttribute("errorGiay", "hangError");
            }
            if (bindingResult.hasFieldErrors("chatLieu")) {
                redirectAttributes.addFlashAttribute("userInput", giay);
                redirectAttributes.addFlashAttribute("errorGiay", "chatLieuError");
            }
            return "redirect:/manage/giay/viewAdd";
        }
        Giay giay1 = new Giay();
        giay1.setMaGiay(giay.getMaGiay());
        giay1.setTenGiay(giay.getTenGiay());
        giay1.setTgThem(new Date());
        giay1.setHang(giay.getHang());
        giay1.setChatLieu(giay.getChatLieu());
        giay1.setTrangThai(1);
        giayService.save(giay1);
        redirectAttributes.addFlashAttribute("message", true);
        return "redirect:/manage/giay";
    }

    @PostMapping("/giay/hang/viewAdd/add")
    public String addHang(@Valid @ModelAttribute("hangAdd") Hang hang, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("maHang")) {
                redirectAttributes.addFlashAttribute("userInput", hang);
                redirectAttributes.addFlashAttribute("errorHang", "maHangError");
            }
            if (bindingResult.hasFieldErrors("tenHang")) {
                redirectAttributes.addFlashAttribute("userInput", hang);
                redirectAttributes.addFlashAttribute("errorHang", "tenHangError");
            }
            return "redirect:/manage/giay/viewAdd";
        }
        Hang hang1 = new Hang();
        hang1.setLogoHang(hang.getLogoHang());
        hang1.setMaHang(hang.getMaHang());
        hang1.setTenHang(hang.getTenHang());
        hang1.setTgThem(new Date());
        hang1.setTrangThai(1);
        hangService.save(hang1);
        redirectAttributes.addFlashAttribute("messageHang", true);
        return "redirect:/manage/giay/viewAdd";
    }

    @PostMapping("/giay/chat-lieu/viewAdd/add")
    public String addChatLieu(@Valid @ModelAttribute("chatLieuAdd") ChatLieu chatLieu, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("maChatLieu")) {
                redirectAttributes.addFlashAttribute("userInput", chatLieu);
                redirectAttributes.addFlashAttribute("errorChatLieu", "maChatLieuError");
            }
            if (bindingResult.hasFieldErrors("tenChatLieu")) {
                redirectAttributes.addFlashAttribute("userInput", chatLieu);
                redirectAttributes.addFlashAttribute("errorChatLieu", "tenChatLieuError");
            }
            return "redirect:/manage/giay/viewAdd";
        }
        ChatLieu chatLieu1 = new ChatLieu();
        chatLieu1.setMaChatLieu(chatLieu.getMaChatLieu());
        chatLieu1.setTenChatLieu(chatLieu.getTenChatLieu());
        chatLieu1.setTgThem(new Date());
        chatLieu1.setTrangThai(1);
        chatLieuService.save(chatLieu1);
        redirectAttributes.addFlashAttribute("messageChatLieu", true);
        return "redirect:/manage/giay/viewAdd";
    }

    @GetMapping("/giay/delete/{id}")
    public String deleteGiay(@PathVariable UUID id) {
        Giay giay = giayService.getByIdGiay(id);
        giay.setTrangThai(0);
        giay.setTgSua(new Date());
        giayService.save(giay);
        // Cập nhật trạng thái của tất cả sản phẩm chi tiết của giay thành 0
        List<ChiTietGiay> chiTietGiays = giayChiTietService.findByGiay(giay);
        for (ChiTietGiay chiTietGiay : chiTietGiays) {
            chiTietGiay.setTrangThai(0);
            giayChiTietService.save(chiTietGiay);
        }
        return "redirect:/manage/giay";
    }

    public void deleteGiayById(UUID idGiay) {
        Giay giay = giayService.getByIdGiay(idGiay);
        giay.setTrangThai(0);
        giay.setTgSua(new Date());
        giayService.save(giay);
        // Cập nhật trạng thái của tất cả sản phẩm chi tiết của giay thành 0
        List<ChiTietGiay> chiTietGiays = giayChiTietService.findByGiay(giay);
        for (ChiTietGiay chiTietGiay : chiTietGiays) {
            chiTietGiay.setTrangThai(0);
            giayChiTietService.save(chiTietGiay);
        }
    }

    @GetMapping("/giay/viewUpdate/{id}")
    public String viewUpdateGiay(@PathVariable UUID id, Model model) {
        Giay giay = giayService.getByIdGiay(id);
        List<Hang> hang = hangService.getALlHang();
        List<ChatLieu> chatLieu = chatLieuService.getAllChatLieu();
        model.addAttribute("giay", giay);
        //
        List<Hang> hangList = hangService.getALlHang();
        Collections.sort(hangList, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("hang", hangList);
        //
        List<ChatLieu> chatLieuList = chatLieuService.getAllChatLieu();
        Collections.sort(chatLieuList, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("chatLieu", chatLieuList);
        //
        model.addAttribute("hangAdd", new Hang());
        model.addAttribute("chatLieuAdd", new ChatLieu());
        return "manage/update-giay";
    }

    @PostMapping("/giay/viewUpdate/{id}")
    public String updateGiay(@PathVariable UUID id, @ModelAttribute("giay") Giay giay) {
        Giay giayDb = giayService.getByIdGiay(id);
        if (giayDb != null) {
            giayDb.setMaGiay(giay.getMaGiay());
            giayDb.setTenGiay(giay.getTenGiay());
            giayDb.setTgSua(giay.getTgSua());
            giayDb.setTrangThai(giay.getTrangThai());
            giayDb.setChatLieu(giay.getChatLieu());
            giayDb.setHang(giay.getHang());
            giayService.save(giayDb);
        }
        // Nếu trạng thái của giay là 1, hãy cập nhật trạng thái của tất cả sản phẩm chi tiết của giay thành 1.
        if (giayDb.getTrangThai() == 1) {
            List<ChiTietGiay> chiTietGiays = giayChiTietService.findByGiay(giayDb);
            for (ChiTietGiay chiTietGiay : chiTietGiays) {
                chiTietGiay.setTrangThai(1);
                giayChiTietService.save(chiTietGiay);
            }
        }
        return "redirect:/manage/giay";
    }

    public void updateGiayById(UUID id) {
        Giay giayDb = giayService.getByIdGiay(id);
        // Nếu trạng thái của giay là 1, hãy cập nhật trạng thái của tất cả sản phẩm chi tiết của giay thành 1.
        if (giayDb.getTrangThai() == 1) {
            List<ChiTietGiay> chiTietGiays = giayChiTietService.findByGiay(giayDb);
            for (ChiTietGiay chiTietGiay : chiTietGiays) {
                chiTietGiay.setTrangThai(1);
                giayChiTietService.save(chiTietGiay);
            }
        }
    }

    @GetMapping("/giay/detail/{id}")
    public String detail(@PathVariable UUID id, Model model, @ModelAttribute("message") String message) {

        //
        Giay giay = giayService.getByIdGiay(id);
        List<ChiTietGiay> listCTGByGiay = giayChiTietService.getCTGByGiay(giay);
        Collections.sort(listCTGByGiay, (a, b) -> b.getTgThem().compareTo(a.getTgThem()));
        model.addAttribute("chiTietGiayList", listCTGByGiay);
        model.addAttribute("idGiay", id);
        List<Giay> giayList = giayService.getAllGiay();
        List<Size> sizeList = sizeService.getAllSize();
        List<MauSac> mauSacList = mauSacService.getALlMauSac();
        model.addAttribute("giayList", giayList);
        model.addAttribute("sizeList", sizeList);
        model.addAttribute("mauSacList", mauSacList);
        //
        if (message == null || !"true".equals(message)) {
            model.addAttribute("message", false);
        }
        return "manage/giay-detail";
    }

    @GetMapping("/giay/export/pdf")
    public void exportToPDFGiay(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=giay_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        List<Giay> listGiay = giayService.getAllGiay();

        PDFExporterGiays exporter = new PDFExporterGiays(listGiay);
        exporter.export(response);
    }

    @GetMapping("/giay/export/excel")
    public void exportToExcelSize(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=giay_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<Giay> lisGiay = giayService.getAllGiay();

        ExcelExporterGiays excelExporter = new ExcelExporterGiays(lisGiay);

        excelExporter.export(response);
    }

    @GetMapping("/giay/filter")
    public String searchGiay(Model model, @RequestParam(name = "searchTerm") String searchTerm) {
        List<Giay> filteredGiays;
        if ("Mã Giày".equals(searchTerm) && "Tên Giày".equals(searchTerm) && "Hãng".equals(searchTerm) && "Chất Liệu".equals(searchTerm)) {
            // Nếu người dùng chọn "Tất cả", hiển thị tất cả dữ liệu
            filteredGiays = giayService.getAllGiay();
        } else {
            // Thực hiện lọc dữ liệu dựa trên selectedSize
            filteredGiays = giayService.fillterGiay(searchTerm);
        }
        model.addAttribute("giay", filteredGiays);
        model.addAttribute("giayAll", giayService.getAllGiay());
        return "manage/giay";
    }

    @PostMapping("/giay/import")
    public String importData(@RequestParam("file") MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                InputStream excelFile = file.getInputStream();
                giayService.importDataFromExcel(excelFile); // Gọi phương thức nhập liệu từ Excel
            } catch (Exception e) {
                e.printStackTrace();
                // Xử lý lỗi
            }
        }
        return "redirect:/manage/giay"; // Chuyển hướng sau khi nhập liệu thành công hoặc không thành công
    }
}
