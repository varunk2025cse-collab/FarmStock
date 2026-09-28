package com.example.FarmStock.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

@Entity
@Table(name = "harvest_batches")
public class HarvestBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Crop is required")
    @ManyToOne
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than zero")
    private Double quantity;

    @NotNull(message = "Harvest date is required")
    private LocalDate harvestDate;

    public HarvestBatch() {
    }

    public HarvestBatch(Crop crop, Double quantity, LocalDate harvestDate) {
        this.crop = crop;
        this.quantity = quantity;
        this.harvestDate = harvestDate;
    }

    public Long getId() {
        return id;
    }

    public Crop getCrop() {
        return crop;
    }

    public Double getQuantity() {
        return quantity;
    }

    public LocalDate getHarvestDate() {
        return harvestDate;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setHarvestDate(LocalDate harvestDate) {
        this.harvestDate = harvestDate;
    }
}