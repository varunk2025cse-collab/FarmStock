package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.service.CropService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<?> createCrop(@Valid @RequestBody Crop crop,
                                        BindingResult result) {

        // If validation fails, send the first error message back
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    result.getFieldError().getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        Crop savedCrop = cropService.saveCrop(crop);
        return new ResponseEntity<>(savedCrop, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Crop>> getAllCrops() {
        List<Crop> crops = cropService.getAllCrops();
        return new ResponseEntity<>(crops, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCropById(@PathVariable Long id) {
        Crop crop = cropService.getCropById(id);

        if (crop == null) {
            return new ResponseEntity<>(
                    "Crop not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(crop, HttpStatus.OK);
    }
}
