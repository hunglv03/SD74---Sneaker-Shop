package com.example.demo.service.impls;

import com.example.demo.repository.CTGViewModelRepository;
import com.example.demo.service.CTGViewModelService;
import com.example.demo.viewModel.CTGViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CTGViewModelServiceImpls implements CTGViewModelService {

    @Autowired
    private CTGViewModelRepository ctgViewModelRepository;

    @Override
    public List<CTGViewModel> getAll() {
        return ctgViewModelRepository.getAll();
    }

    @Override
    public Page<CTGViewModel> getAllPage(Pageable pageable) {
        return ctgViewModelRepository.getAllPageable(pageable);
    }

    @Override
    public List<CTGViewModel> getAllSoldOff() {
        return ctgViewModelRepository.getAllOutOfStock();
    }

    @Override
    public CTGViewModel findByIDGiay(UUID idGiay) {
        return ctgViewModelRepository.findByGiay(idGiay);
    }

    @Override
    public List<CTGViewModel> findByIDHang(String idHang) {
        return ctgViewModelRepository.findByHang(idHang);
    }

    @Override
    public Page<CTGViewModel> getAllByPriceHighToLow(Pageable pageable) {
        return ctgViewModelRepository.listCTGVMHTL(pageable);
    }

    @Override
    public Page<CTGViewModel> getAllByPriceLowToHigh(Pageable pageable) {
        return ctgViewModelRepository.listCTGVMLTH(pageable);
    }
}
