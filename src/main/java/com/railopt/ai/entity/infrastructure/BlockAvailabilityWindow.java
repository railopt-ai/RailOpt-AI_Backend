package com.railopt.ai.entity.infrastructure;

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

/** Persistence mapping for table `block_availability_windows`. */
@Entity
@Table(name = "block_availability_windows")
@Getter
@Setter
@NoArgsConstructor
public class BlockAvailabilityWindow {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "block_section_id", nullable = false, columnDefinition = "uuid")
    private BlockSection blockSection;

    @Column(name = "available_from", columnDefinition = "timestamp")
    private LocalDateTime availableFrom;

    @Column(name = "available_until", columnDefinition = "timestamp")
    private LocalDateTime availableUntil;

    @Column(name = "availability_type", columnDefinition = "varchar")
    private String availabilityType;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "restriction_reason", columnDefinition = "text")
    private String restrictionReason;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
