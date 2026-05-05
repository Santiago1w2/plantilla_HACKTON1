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

    public Guardian(){}

    public Guardian(String displayName, String email, String notificationEmail, Instant createdAt){
        this.displayName = displayName;
        this.email = email;
        this.notificationEmail = notificationEmail;
        this.createdAt = createdAt;
    }



    // --- Getters ---

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getNotificationEmail() {
        return notificationEmail;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    // --- Setters ---

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNotificationEmail(String notificationEmail) {
        this.notificationEmail = notificationEmail;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}


