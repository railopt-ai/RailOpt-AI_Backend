package com.railopt.ai.model.scheduling;

import java.time.LocalDateTime;

import com.railopt.ai.model.maintenance.MaintenanceResource;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `scheduled_block_resources`. */
@Entity
@Table(name = "scheduled_block_resources")
@Getter
@Setter
@NoArgsConstructor
public class ScheduledBlockResource {

    @EmbeddedId
    private ScheduledBlockResourceId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("scheduledBlockId")
    @JoinColumn(name = "scheduled_block_id", nullable = false, columnDefinition = "uuid")
    private ScheduledBlock scheduledBlock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("resourceId")
    @JoinColumn(name = "resource_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceResource resource;

    @Column(name = "quantity_allocated", columnDefinition = "integer")
    private Integer quantityAllocated;

    @Column(name = "allocated_start_at", columnDefinition = "timestamp")
    private LocalDateTime allocatedStartAt;

    @Column(name = "allocated_end_at", columnDefinition = "timestamp")
    private LocalDateTime allocatedEndAt;

}
