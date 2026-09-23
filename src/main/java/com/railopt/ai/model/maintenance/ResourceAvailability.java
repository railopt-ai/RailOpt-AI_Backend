package com.railopt.ai.model.maintenance;

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

/** Persistence mapping for table `resource_availability`. */
@Entity
@Table(name = "resource_availability")
@Getter
@Setter
@NoArgsConstructor
public class ResourceAvailability {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resource_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceResource resource;

    @Column(name = "available_from", columnDefinition = "timestamp")
    private LocalDateTime availableFrom;

    @Column(name = "available_until", columnDefinition = "timestamp")
    private LocalDateTime availableUntil;

    @Column(name = "available_quantity", columnDefinition = "integer")
    private Integer availableQuantity;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

}
