package org.test.week06lab01.CareResponse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import jakarta.persistence.*;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;

import java.time.Instant;

@Entity
@Table(name = "care_response", uniqueConstraints = {
        @UniqueConstraint(columnNames = "signal_id")
})
public class CareResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "signal_id", nullable = false, unique = true)
    private TropelSignal signal;

    @Column(nullable = false)
    private String responseCode;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Instant createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public TropelSignal getSignal() {
        return signal;
    }

    public void setSignal(TropelSignal signal) {
        this.signal = signal;
    }
}
