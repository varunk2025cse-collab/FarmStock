package com.example.FarmStock.repository;

import com.example.FarmStock.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
    
}