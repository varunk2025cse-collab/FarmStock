package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.SaleService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
public class RevenuePageController {

    private final CropService cropService;
    private final SaleService saleService;

    public RevenuePageController(CropService cropService, SaleService saleService) {
        this.cropService = cropService;
        this.saleService = saleService;
    }

    // The same page shows the form, and the report after the form is submitted
    @GetMapping("/revenue")
    public String showRevenuePage(
            @RequestParam(required = false) Long cropId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Model model) {

        model.addAttribute("crops", cropService.getAllCrops());

        // Page opened for the first time: only show the form
        if (cropId == null || from == null || to == null) {
            return "revenue";
        }

        Crop crop = cropService.getCropById(cropId);

        if (crop == null) {
            model.addAttribute("errorMessage", "Please select a valid crop");
            return "revenue";
        }

        if (from.isAfter(to)) {
            model.addAttribute("errorMessage", "From date must be before To date");
            return "revenue";
        }

        model.addAttribute("selectedCrop", crop);
        model.addAttribute("from", from);
        model.addAttribute("to", to);
        model.addAttribute("sales", saleService.getSalesBetween(cropId, from, to));
        model.addAttribute("totalQuantity", saleService.getTotalQuantitySold(cropId, from, to));
        model.addAttribute("totalRevenue", saleService.getTotalRevenue(cropId, from, to));

        return "revenue";
    }
}
