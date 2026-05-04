package org.test.week06lab01.guardian.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Instant;


@Entity
public class Guardian {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String displayName;
    private String email;
    private String notificationEmail;
    private Instant createdAt;



}
