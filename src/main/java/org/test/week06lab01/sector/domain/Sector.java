package org.test.week06lab01.sector.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Entity
public class Sector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String sectorCode;

    @NotBlank
    private String climate; // Valores: PIXEL_FOREST, NEON_CAVE, CLOUD_AQUARIUM, RETRO_ARCADE

    @NotNull
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    private Integer capacity;

    private Integer currentLoad;

    @Min(0)
    @Max(100)
    private Integer stabilityLevel;

    private Instant createdAt;

    // --- Constructores ---

    public Sector() {
    }

    public Sector(String sectorCode, String climate, int capacity, int currentLoad, Instant createdAt) {
        this.sectorCode = sectorCode;
        this.climate = climate;
        this.capacity = capacity;
        this.currentLoad = currentLoad;
        this.createdAt = createdAt;
    }

    // --- Getters ---

    public Long getId() {
        return id;
    }

    public String getSectorCode() {
        return sectorCode;
    }

    public String getClimate() {
        return climate;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentLoad() {
        return currentLoad;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // --- Setters ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setSectorCode(String sectorCode) {
        this.sectorCode = sectorCode;
    }

    public void setClimate(String climate) {
        this.climate = climate;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setCurrentLoad(int currentLoad) {
        this.currentLoad = currentLoad;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

}
