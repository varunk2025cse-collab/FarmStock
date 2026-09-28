package com.example.FarmStock.repository;

import com.example.FarmStock.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByCropId(Long cropId);
}
