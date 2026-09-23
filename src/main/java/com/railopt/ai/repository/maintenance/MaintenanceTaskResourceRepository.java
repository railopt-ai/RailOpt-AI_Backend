package com.railopt.ai.repository.maintenance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.maintenance.MaintenanceTaskResource;
import com.railopt.ai.entity.maintenance.MaintenanceTaskResourceId;

/** Repository interface for {@link MaintenanceTaskResource}. */
@Repository
public interface MaintenanceTaskResourceRepository extends JpaRepository<MaintenanceTaskResource, MaintenanceTaskResourceId> {
}
