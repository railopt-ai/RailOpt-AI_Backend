package com.railopt.ai.repository.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.infrastructure.BlockSection;

/** Repository interface for {@link BlockSection}. */
@Repository
public interface BlockSectionRepository extends JpaRepository<BlockSection, UUID> {
}
