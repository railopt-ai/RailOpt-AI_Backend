package com.railopt.ai.model.maintenance;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.model.asset.Asset;
import com.railopt.ai.model.organization.Department;
import com.railopt.ai.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/** Persistence mapping for table `maintenance_tasks`. */
@Entity
@Table(
        name = "maintenance_tasks",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "task_code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceTask {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asset_id", nullable = false, columnDefinition = "uuid")
    private Asset asset;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "defect_id", nullable = true, columnDefinition = "uuid")
    private Defect defect;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "maintenance_type_id", nullable = false, columnDefinition = "uuid")
    private MaintenanceType maintenanceType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_department_id", nullable = false, columnDefinition = "uuid")
    private Department assignedDepartment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "assigned_by", nullable = false, columnDefinition = "uuid")
    private User assignedBy;

    @Column(name = "task_code", nullable = false, unique = true, columnDefinition = "varchar")
    private String taskCode;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "priority", columnDefinition = "varchar")
    private String priority;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "estimated_duration_hours", columnDefinition = "numeric")
    private BigDecimal estimatedDurationHours;

    @Column(name = "actual_duration_hours", columnDefinition = "numeric")
    private BigDecimal actualDurationHours;

    @Column(name = "requested_at", columnDefinition = "timestamp")
    private LocalDateTime requestedAt;

    @Column(name = "scheduled_at", columnDefinition = "timestamp")
    private LocalDateTime scheduledAt;

    @Column(name = "completed_at", columnDefinition = "timestamp")
    private LocalDateTime completedAt;

    @Column(name = "deadline_at", columnDefinition = "timestamp")
    private LocalDateTime deadlineAt;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
