package com.railopt.ai.entity.scheduling;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Composite primary key for table `scheduling_run_tasks`. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SchedulingRunTaskId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "scheduling_run_id", nullable = false, columnDefinition = "uuid")
    private UUID schedulingRunId;

    @Column(name = "maintenance_task_id", nullable = false, columnDefinition = "uuid")
    private UUID maintenanceTaskId;

}
