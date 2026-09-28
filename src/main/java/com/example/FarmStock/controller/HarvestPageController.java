package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.model.HarvestBatch;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.HarvestBatchService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HarvestPageController {

    private final HarvestBatchService harvestBatchService;
    private final CropService cropService;

    public HarvestPageController(HarvestBatchService harvestBatchService,
                                 CropService cropService) {
        this.harvestBatchService = harvestBatchService;
        this.cropService = cropService;
    }

    @GetMapping("/harvests")
    public String showHarvestsPage(Model model) {

        model.addAttribute("crops", cropService.getAllCrops());
        model.addAttribute("harvestBatches", harvestBatchService.getAllHarvestBatches());

        return "harvests";
    }

    @PostMapping("/harvests")
    public String addHarvestBatch(@Valid @ModelAttribute HarvestBatch harvestBatch,
                                  BindingResult result,
                                  Model model) {

        // If the form has errors, show the same page with the message
        if (result.hasErrors()) {
            model.addAttribute("errorMessage",
                    result.getFieldError().getDefaultMessage());
            return showHarvestsPage(model);
        }

        // Check that the selected crop really exists
        Long cropId = harvestBatch.getCrop().getId();
        Crop crop = null;
        if (cropId != null) {
            crop = cropService.getCropById(cropId);
        }

        if (crop == null) {
            model.addAttribute("errorMessage", "Please select a valid crop");
            return showHarvestsPage(model);
        }

        harvestBatch.setCrop(crop);
        harvestBatchService.saveHarvestBatch(harvestBatch);

        return "redirect:/harvests";
    }
}
