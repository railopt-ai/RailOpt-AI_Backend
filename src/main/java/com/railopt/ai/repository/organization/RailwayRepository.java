package com.railopt.ai.repository.organization;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.organization.Railway;

/** Repository interface for {@link Railway}. */
@Repository
public interface RailwayRepository extends JpaRepository<Railway, UUID> {
}
