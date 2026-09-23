package com.railopt.ai.model.maintenance;

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

/** Persistence mapping for table `maintenance_task_resources`. */
@Entity
@Table(name = "maintenance_task_resources")
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceTaskResource {

    @EmbeddedId
    private MaintenanceTaskResourceId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("maintenanceTaskId")
    @JoinColumn(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceTask maintenanceTask;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("resourceId")
    @JoinColumn(name = "resource_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceResource resource;

    @Column(name = "required_quantity", columnDefinition = "integer")
    private Integer requiredQuantity;

}
