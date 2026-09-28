package com.example.FarmStock.controller;

import com.example.FarmStock.model.Sale;
import com.example.FarmStock.service.SaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    public ResponseEntity<?> createSale(@RequestBody Sale sale) {

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
}