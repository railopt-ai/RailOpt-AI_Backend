package com.railopt.ai.repository.organization;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.organization.RailwayZone;

/** Repository interface for {@link RailwayZone}. */
@Repository
public interface RailwayZoneRepository extends JpaRepository<RailwayZone, UUID> {
}
