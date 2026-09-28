package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.model.Sale;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.SaleService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SalePageController {

    private final SaleService saleService;
    private final CropService cropService;

    public SalePageController(SaleService saleService, CropService cropService) {
        this.saleService = saleService;
        this.cropService = cropService;
    }

    @GetMapping("/sales")
    public String showSalesPage(Model model) {

        model.addAttribute("crops", cropService.getAllCrops());
        model.addAttribute("sales", saleService.getAllSales());

        return "sales";
    }

    @PostMapping("/sales")
    public String addSale(@Valid @ModelAttribute Sale sale,
                          BindingResult result,
                          Model model) {

        // If the form has errors, show the same page with the message
        if (result.hasErrors()) {
            model.addAttribute("errorMessage",
                    result.getFieldError().getDefaultMessage());
            return showSalesPage(model);
        }

        // Check that the selected crop really exists
        Long cropId = sale.getCrop().getId();
        Crop crop = null;
        if (cropId != null) {
            crop = cropService.getCropById(cropId);
        }

        if (crop == null) {
            model.addAttribute("errorMessage", "Please select a valid crop");
            return showSalesPage(model);
        }

        sale.setCrop(crop);

        // saveSale returns null when there is not enough stock
        Sale savedSale = saleService.saveSale(sale);

        if (savedSale == null) {
            Double currentStock = saleService.getCurrentStock(cropId);
            model.addAttribute("errorMessage",
                    "Insufficient stock. Available stock of " + crop.getName()
                            + ": " + currentStock + " kg");
            return showSalesPage(model);
        }

        return "redirect:/sales";
    }
}
