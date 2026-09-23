package com.railopt.ai.repository.maintenance;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.maintenance.Defect;

/** Repository interface for {@link Defect}. */
@Repository
public interface DefectRepository extends JpaRepository<Defect, UUID> {
}
