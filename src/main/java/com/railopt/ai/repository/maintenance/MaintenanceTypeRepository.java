package com.railopt.ai.repository.maintenance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.maintenance.MaintenanceType;

/** Repository interface for {@link MaintenanceType}. */
@Repository
public interface MaintenanceTypeRepository extends JpaRepository<MaintenanceType, UUID> {
}
