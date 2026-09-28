package com.example.FarmStock.repository;

import com.example.FarmStock.model.HarvestBatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HarvestBatchRepository extends JpaRepository<HarvestBatch, Long> {

    List<HarvestBatch> findByCropId(Long cropId);
}
