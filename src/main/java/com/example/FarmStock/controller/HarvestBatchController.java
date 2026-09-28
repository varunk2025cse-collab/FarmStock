package com.example.FarmStock.controller;

import com.example.FarmStock.model.Crop;
import com.example.FarmStock.model.HarvestBatch;
import com.example.FarmStock.service.CropService;
import com.example.FarmStock.service.HarvestBatchService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvest-batches")
public class HarvestBatchController {

    private final HarvestBatchService harvestBatchService;
    private final CropService cropService;

    public HarvestBatchController(HarvestBatchService harvestBatchService,
                                  CropService cropService) {
        this.harvestBatchService = harvestBatchService;
        this.cropService = cropService;
    }

    @PostMapping
    public ResponseEntity<?> createHarvestBatch(
            @Valid @RequestBody HarvestBatch harvestBatch,
            BindingResult result) {

        // If validation fails, send the first error message back
        if (result.hasErrors()) {
            return new ResponseEntity<>(
                    result.getFieldError().getDefaultMessage(),
                    HttpStatus.BAD_REQUEST
            );
        }

        // Check that the crop really exists in the database
        Long cropId = harvestBatch.getCrop().getId();
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

        harvestBatch.setCrop(crop);

        HarvestBatch savedBatch =
                harvestBatchService.saveHarvestBatch(harvestBatch);

        return new ResponseEntity<>(savedBatch, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<HarvestBatch>> getAllHarvestBatches() {

        List<HarvestBatch> batches =
                harvestBatchService.getAllHarvestBatches();

        return new ResponseEntity<>(batches, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHarvestBatchById(
            @PathVariable Long id) {

        HarvestBatch batch =
                harvestBatchService.getHarvestBatchById(id);

        if (batch == null) {
            return new ResponseEntity<>(
                    "Harvest batch not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }

        return new ResponseEntity<>(batch, HttpStatus.OK);
    }
}
