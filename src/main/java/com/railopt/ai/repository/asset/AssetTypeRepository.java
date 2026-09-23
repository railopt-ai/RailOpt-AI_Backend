package com.railopt.ai.repository.asset;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.asset.AssetType;

/** Repository interface for {@link AssetType}. */
@Repository
public interface AssetTypeRepository extends JpaRepository<AssetType, UUID> {
}
