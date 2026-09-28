package com.example.FarmStock.repository;

import com.example.FarmStock.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {

}
