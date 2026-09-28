package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.model.Sale;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;
    private final CropService cropService;

    public SaleController(SaleService saleService, CropService cropService) {
        this.saleService = saleService;
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<?> createSale(@Valid @RequestBody Sale sale,
                                        BindingResult result) {

        // If validation fails, send the first error message back
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    result.getFieldError().getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Check that the crop really exists in the database
        Long cropId = sale.getCrop().getId();
        Crop crop = null;
        if (cropId != null) {
            crop = cropService.getCropById(cropId);
        }

        if (crop == null) {
            return new ResponseEntity<>(
                    "Crop not found with id: " + cropId,
                    HttpStatus.NOT_FOUND
            );
        }

        sale.setCrop(crop);

        Sale savedSale = saleService.saveSale(sale);

        if (savedSale == null) {
            Double currentStock =
                    saleService.getCurrentStock(sale.getCrop().getId());

            return new ResponseEntity<>(
                    "Insufficient stock. Available stock: " + currentStock,
                    HttpStatus.BAD_REQUEST
            );
        }

        return new ResponseEntity<>(savedSale, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Sale>> getAllSales() {

        List<Sale> sales = saleService.getAllSales();

        return new ResponseEntity<>(sales, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSaleById(@PathVariable Long id) {

        Sale sale = saleService.getSaleById(id);

        if (sale == null) {
            return new ResponseEntity<>(
                    "Sale not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(sale, HttpStatus.OK);
    }

    @GetMapping("/stock/{cropId}")
    public ResponseEntity<Double> getCurrentStock(
            @PathVariable Long cropId) {

        Double currentStock = saleService.getCurrentStock(cropId);

        return new ResponseEntity<>(currentStock, HttpStatus.OK);
    }

    @GetMapping("/revenue/{cropId}")
    public ResponseEntity<?> getTotalRevenue(
            @PathVariable Long cropId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        Crop crop = cropService.getCropById(cropId);

        if (crop == null) {
            return new ResponseEntity<>(
                    "Crop not found with id: " + cropId,
                    HttpStatus.NOT_FOUND
            );
        }

        if (from.isAfter(to)) {
            return new ResponseEntity<>(
                    "From date must be before To date",
                    HttpStatus.BAD_REQUEST
            );
        }

        Double totalRevenue = saleService.getTotalRevenue(cropId, from, to);

        return new ResponseEntity<>(totalRevenue, HttpStatus.OK);
    }
}
