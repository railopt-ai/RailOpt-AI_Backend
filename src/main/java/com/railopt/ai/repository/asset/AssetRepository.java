package com.railopt.ai.repository.asset;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.model.asset.Asset;

/** Repository interface for {@link Asset}. */
@Repository
public interface AssetRepository extends JpaRepository<Asset, UUID> {
}
