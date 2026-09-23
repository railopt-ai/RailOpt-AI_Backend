package com.railopt.ai.model.maintenance;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Composite primary key for table `maintenance_task_block_requirements`. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MaintenanceTaskBlockRequirementId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private UUID maintenanceTaskId;

    @Column(name = "block_section_id", nullable = false, columnDefinition = "uuid")
    private UUID blockSectionId;

}
