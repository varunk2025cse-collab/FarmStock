package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.service.CropService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CropPageController {

    private final CropService cropService;

    public CropPageController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping("/crops")
    public String showCropsPage(Model model) {

        model.addAttribute("crops", cropService.getAllCrops());

        return "crops";
    }

    @PostMapping("/crops")
    public String addCrop(@Valid @ModelAttribute Crop crop,
                          BindingResult result,
                          Model model) {

        // If the form has errors, show the same page with the message
        if (result.hasErrors()) {
            model.addAttribute("errorMessage",
                    result.getFieldError().getDefaultMessage());
            model.addAttribute("crops", cropService.getAllCrops());
            return "crops";
        }

        cropService.saveCrop(crop);

        return "redirect:/crops";
    }
}
