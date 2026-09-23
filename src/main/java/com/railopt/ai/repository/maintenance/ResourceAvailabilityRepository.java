package com.railopt.ai.repository.maintenance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.maintenance.ResourceAvailability;

/** Repository interface for {@link ResourceAvailability}. */
@Repository
public interface ResourceAvailabilityRepository extends JpaRepository<ResourceAvailability, UUID> {
}
