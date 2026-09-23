package com.railopt.ai.entity.maintenance;

import java.time.LocalDateTime;
import java.util.UUID;

import com.railopt.ai.entity.organization.Department;

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


/** Persistence mapping for table `maintenance_resources`. */
@Entity
@Table(
        name = "maintenance_resources",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "code")
        }
)
@Getter
@Setter
@NoArgsConstructor
public class MaintenanceResource {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "department_id", nullable = false, columnDefinition = "uuid")
    private Department department;

    @Column(name = "code", nullable = false, unique = true, columnDefinition = "varchar")
    private String code;

    @Column(name = "name", columnDefinition = "varchar")
    private String name;

    @Column(name = "resource_type", columnDefinition = "varchar")
    private String resourceType;

    @Column(name = "capacity", columnDefinition = "integer")
    private Integer capacity;

    @Column(name = "status", columnDefinition = "varchar")
    private String status;

    @Column(name = "created_at", columnDefinition = "timestamp")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "timestamp")
    private LocalDateTime updatedAt;

}
