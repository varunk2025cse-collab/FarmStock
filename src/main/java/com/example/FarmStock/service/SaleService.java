package com.example.FarmStock.service;

import com.example.FarmStock.model.HarvestBatch;
import com.example.FarmStock.model.Sale;
import com.example.FarmStock.repository.HarvestBatchRepository;
import com.example.FarmStock.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final HarvestBatchRepository harvestBatchRepository;

    public SaleService(
            SaleRepository saleRepository,
            HarvestBatchRepository harvestBatchRepository) {

        this.saleRepository = saleRepository;
        this.harvestBatchRepository = harvestBatchRepository;
    }

    public Sale saveSale(Sale sale) {

        Long cropId = sale.getCrop().getId();

        Double currentStock = getCurrentStock(cropId);

        // Overselling check: if not enough stock, do not save
        if (sale.getQuantity() > currentStock) {
            return null;
        }

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

    public Double getCurrentStock(Long cropId) {

        List<HarvestBatch> harvestBatches =
                harvestBatchRepository.findByCropId(cropId);

        List<Sale> sales =
                saleRepository.findByCropId(cropId);

        double totalHarvested = 0;
        double totalSold = 0;

        for (HarvestBatch batch : harvestBatches) {
            totalHarvested += batch.getQuantity();
        }

        for (Sale sale : sales) {
            totalSold += sale.getQuantity();
        }

        return totalHarvested - totalSold;
    }
}