package com.example.FarmStock.service;

import com.example.FarmStock.model.Sale;
import com.example.FarmStock.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Sale saveSale(Sale sale) {
        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Sale not found with id: " + id));
    }
}