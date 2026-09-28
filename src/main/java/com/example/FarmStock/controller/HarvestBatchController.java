package com.example.FarmStock.controller;

import com.example.FarmStock.model.HarvestBatch;
import com.example.FarmStock.service.HarvestBatchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/harvest-batches")
public class HarvestBatchController {

    private final HarvestBatchService harvestBatchService;

    public HarvestBatchController(HarvestBatchService harvestBatchService) {
        this.harvestBatchService = harvestBatchService;
    }

    @PostMapping
    public ResponseEntity<HarvestBatch> createHarvestBatch(
            @RequestBody HarvestBatch harvestBatch) {

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