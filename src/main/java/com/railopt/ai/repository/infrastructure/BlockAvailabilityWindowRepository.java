package com.railopt.ai.repository.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.infrastructure.BlockAvailabilityWindow;

/** Repository interface for {@link BlockAvailabilityWindow}. */
@Repository
public interface BlockAvailabilityWindowRepository extends JpaRepository<BlockAvailabilityWindow, UUID> {
}
