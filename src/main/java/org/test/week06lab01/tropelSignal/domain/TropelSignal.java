package org.test.week06lab01.tropelSignal.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import org.antlr.v4.runtime.misc.NotNull;

import java.time.Instant;

@Entity
@Table(name = "tropel_signal")
public class TropelSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tropel_id", nullable = false)
    @NotNull
    private Tropel tropel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "guardian_id", nullable = false)
    @NotNull
    private Guardian guardian;

    @Column(name = "sender_tag")
    private String senderTag;

    @Column(name = "raw_content", columnDefinition = "TEXT", nullable = false)
    @NotNull
    @Size(min = 10, message = "rawContent debe tener al menos 10 caracteres")
    private String rawContent;

    @Column(name = "signal_type")
    private String signalType;

    @Column(name = "severity")
    private String severity;

    @Column(name = "assigned_unit")
    private String assignedUnit;

    @Column(name = "recommended_action", columnDefinition = "TEXT")
    private String recommendedAction;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}

