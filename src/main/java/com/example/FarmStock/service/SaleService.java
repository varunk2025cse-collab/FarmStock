package com.example.FarmStock.service;

import com.example.FarmStock.model.HarvestBatch;
import com.example.FarmStock.model.Sale;
import com.example.FarmStock.repository.HarvestBatchRepository;
import com.example.FarmStock.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        // returns null when the sale is not in the database
        return saleRepository.findById(id)
                .orElse(null);
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

    public Double getTotalRevenue(Long cropId, LocalDate from, LocalDate to) {

        List<Sale> sales =
                saleRepository.findByCropIdAndSaleDateBetween(cropId, from, to);

        double totalRevenue = 0;

        // Revenue = quantity x price per unit
        for (Sale sale : sales) {
            totalRevenue += sale.getQuantity() * sale.getPricePerUnit();
        }

        return totalRevenue;
    }

    // ---------- Totals for the dashboard (all crops) ----------

    public Double getTotalHarvested() {

        List<HarvestBatch> harvestBatches = harvestBatchRepository.findAll();

        double totalHarvested = 0;

        for (HarvestBatch batch : harvestBatches) {
            totalHarvested += batch.getQuantity();
        }

        return totalHarvested;
    }

    public Double getTotalSold() {

        List<Sale> sales = saleRepository.findAll();

        double totalSold = 0;

        for (Sale sale : sales) {
            totalSold += sale.getQuantity();
        }

        return totalSold;
    }

    public Double getTotalStock() {
        return getTotalHarvested() - getTotalSold();
    }

    public Double getTotalRevenueOfAllSales() {

        List<Sale> sales = saleRepository.findAll();

        double totalRevenue = 0;

        for (Sale sale : sales) {
            totalRevenue += sale.getQuantity() * sale.getPricePerUnit();
        }

        return totalRevenue;
    }
}
