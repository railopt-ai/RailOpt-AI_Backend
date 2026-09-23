package com.railopt.ai.repository.infrastructure;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.railopt.ai.entity.infrastructure.Track;

/** Repository interface for {@link Track}. */
@Repository
public interface TrackRepository extends JpaRepository<Track, UUID> {
}
