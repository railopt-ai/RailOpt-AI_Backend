package com.railopt.ai.model.user;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Persistence mapping for table `users`. */
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "google_subject_id"),
                @UniqueConstraint(columnNames = "email")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @Column(name = "google_subject_id", nullable = false, unique = true, columnDefinition = "varchar")
    private String googleSubjectId;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "varchar")
    private String email;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "profile_picture_url", columnDefinition = "text")
    private String profilePictureUrl;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "last_login_at", columnDefinition = "timestamp")
    private LocalDateTime lastLoginAt;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
