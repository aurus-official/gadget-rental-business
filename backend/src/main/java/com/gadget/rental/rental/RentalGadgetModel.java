package com.gadget.rental.rental;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name = "rentalGadgetInfo")
@Table(name = "rentalGadgetInfo")
public class RentalGadgetModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "description", length = 512)
    private String description;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "last_updated")
    private ZonedDateTime lastUpdated;

    @Column(name = "image_dir")
    private String imageDir;

    @Convert(converter = RentalGadgetStatusConverter.class)
    @Column(name = "status", columnDefinition = "varchar(255) default 'AVAILABLE'")
    private RentalGadgetStatus status = RentalGadgetStatus.AVAILABLE;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageDir() {
        return imageDir;
    }

    public void setImageDir(String imageDir) {
        this.imageDir = imageDir;
    }

    public ZonedDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(ZonedDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public RentalGadgetStatus getStatus() {
        return status;
    }

    public void setStatus(RentalGadgetStatus status) {
        this.status = status;
    }
}
