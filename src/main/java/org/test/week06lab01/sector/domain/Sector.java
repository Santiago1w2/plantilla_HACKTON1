package org.test.week06lab01.sector.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;

@Entity
public class Sector {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String sectorCode;
    private String climate;
    private int capacity;
    private int currentLoad;
    private int stabilityLevel;
    private Instant createdAt;
}
