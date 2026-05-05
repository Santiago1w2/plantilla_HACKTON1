package org.test.week06lab01.Tropel;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.test.week06lab01.guardian.domain.Guardian;
import org.test.week06lab01.sector.domain.Sector;

import java.time.Instant;

import java.time.Instant;

@Entity
@Getter
@Setter
public class Tropel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 40)
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Species species;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VitalState vitalState = VitalState.ESTABLE;

    @Min(0)
    @Max(100)
    private Integer energyLevel = 80;

    @Min(0)
    @Max(100)
    private Integer chaosIndex = 10;

    @Min(0)
    @Max(5)
    private Integer mutationStage = 0;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sector_id", nullable = false)
    private Sector sector;

    @ManyToOne(optional = false)
    @JoinColumn(name = "guardian_id", nullable = false)
    private Guardian guardian;


    private Instant createdAt;

    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }}
