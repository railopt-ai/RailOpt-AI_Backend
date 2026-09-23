package com.railopt.ai.repository.organization;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.organization.RailwayDivision;

/** Repository interface for {@link RailwayDivision}. */
@Repository
public interface RailwayDivisionRepository extends JpaRepository<RailwayDivision, UUID> {
}
