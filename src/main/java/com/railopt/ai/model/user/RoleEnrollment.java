package com.railopt.ai.model.user;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Persistence mapping for table `role_enrollments`. */
@Entity
@Table(name = "role_enrollments")
@Getter
@Setter
@NoArgsConstructor
public class RoleEnrollment {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "uuid")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false, columnDefinition = "uuid")
    private Role role;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "request_reason", columnDefinition = "text")
    private String requestReason;

    @Column(name = "requested_at", columnDefinition = "timestamp")
    private LocalDateTime requestedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "reviewed_by", nullable = true, columnDefinition = "uuid")
    private User reviewedBy;

    @Column(name = "reviewed_at", columnDefinition = "timestamp")
    private LocalDateTime reviewedAt;

    @Column(name = "review_reason", columnDefinition = "text")
    private String reviewReason;

}
