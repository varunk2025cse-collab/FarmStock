package com.example.FarmStock.controller;

import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.SaleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final CropService cropService;
    private final SaleService saleService;

    public DashboardController(CropService cropService, SaleService saleService) {
        this.cropService = cropService;
        this.saleService = saleService;
    }

    @GetMapping("/")
    public String showDashboard(Model model) {

        model.addAttribute("totalCrops", cropService.getAllCrops().size());
        model.addAttribute("totalHarvested", saleService.getTotalHarvested());
        model.addAttribute("totalSold", saleService.getTotalSold());
        model.addAttribute("currentStock", saleService.getTotalStock());
        model.addAttribute("totalRevenue", saleService.getTotalRevenueOfAllSales());

        return "dashboard";
    }
}
