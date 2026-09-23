package com.railopt.ai.repository.maintenance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.maintenance.MaintenanceTaskBlockRequirement;
import com.railopt.ai.model.maintenance.MaintenanceTaskBlockRequirementId;

/** Repository interface for {@link MaintenanceTaskBlockRequirement}. */
@Repository
public interface MaintenanceTaskBlockRequirementRepository extends JpaRepository<MaintenanceTaskBlockRequirement, MaintenanceTaskBlockRequirementId> {
}
