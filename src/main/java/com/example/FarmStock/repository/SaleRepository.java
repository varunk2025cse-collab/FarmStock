package com.example.FarmStock.repository;

import com.example.FarmStock.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    List<Sale> findByCropId(Long cropId);

    // Sales of one crop between two dates (both dates included)
    List<Sale> findByCropIdAndSaleDateBetween(Long cropId,
                                              LocalDate from,
                                              LocalDate to);
}
