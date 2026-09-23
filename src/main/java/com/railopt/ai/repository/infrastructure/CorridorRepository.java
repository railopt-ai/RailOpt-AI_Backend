package com.railopt.ai.repository.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.infrastructure.Corridor;

/** Repository interface for {@link Corridor}. */
@Repository
public interface CorridorRepository extends JpaRepository<Corridor, UUID> {
}
