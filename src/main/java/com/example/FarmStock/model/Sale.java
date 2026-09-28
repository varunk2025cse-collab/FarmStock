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
@Table(name = "sales")
public class Sale {

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

    @NotNull(message = "Price per unit is required")
    @Positive(message = "Price per unit must be greater than zero")
    private Double pricePerUnit;

    @NotNull(message = "Sale date is required")
    private LocalDate saleDate;

    public Sale() {
    }

    public Sale(Crop crop, Double quantity, Double pricePerUnit, LocalDate saleDate) {
        this.crop = crop;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.saleDate = saleDate;
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

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public void setCrop(Crop crop) {
        this.crop = crop;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public void setPricePerUnit(Double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setSaleDate(LocalDate saleDate) {
        this.saleDate = saleDate;
    }
}