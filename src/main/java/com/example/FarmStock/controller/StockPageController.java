package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.SaleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StockPageController {

    private final CropService cropService;
    private final SaleService saleService;

    public StockPageController(CropService cropService, SaleService saleService) {
        this.cropService = cropService;
        this.saleService = saleService;
    }

    @GetMapping("/stock")
    public String showStockPage(Model model) {

        List<Crop> crops = cropService.getAllCrops();

        // key = crop, value = current stock (calculated by SaleService)
        Map<Crop, Double> stockMap = new LinkedHashMap<>();

        for (Crop crop : crops) {
            stockMap.put(crop, saleService.getCurrentStock(crop.getId()));
        }

        model.addAttribute("stockMap", stockMap);

        return "stock";
    }
}
