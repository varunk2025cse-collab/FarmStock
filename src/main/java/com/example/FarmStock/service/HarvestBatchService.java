package com.example.FarmStock.service;

import com.example.FarmStock.model.HarvestBatch;
import com.example.FarmStock.repository.HarvestBatchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HarvestBatchService {

    private final HarvestBatchRepository harvestBatchRepository;

    public HarvestBatchService(HarvestBatchRepository harvestBatchRepository) {
        this.harvestBatchRepository = harvestBatchRepository;
    }

    public HarvestBatch saveHarvestBatch(HarvestBatch harvestBatch) {
        return harvestBatchRepository.save(harvestBatch);
    }

    public List<HarvestBatch> getAllHarvestBatches() {
        return harvestBatchRepository.findAll();
    }

    public HarvestBatch getHarvestBatchById(Long id) {
        return harvestBatchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Harvest batch not found with id: " + id));
    }
}