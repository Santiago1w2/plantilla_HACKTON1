package org.test.week06lab01.NotificationLog.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.test.week06lab01.tropelSignal.domain.TropelSignal;

import java.time.Instant;

@Entity
public class NotificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "signal_id", nullable = false)
    @NotNull
    private TropelSignal signal;

    @Column(name = "recipient_email")
    private String recipientEmail;

    @Column(name = "subject")
    private String subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "notif_status")
    private NotifStatus notifStatus;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "sent_at")
    private Instant sentAt;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;
}
