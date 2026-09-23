package com.railopt.ai.repository.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.infrastructure.Station;

/** Repository interface for {@link Station}. */
@Repository
public interface StationRepository extends JpaRepository<Station, UUID> {
}
