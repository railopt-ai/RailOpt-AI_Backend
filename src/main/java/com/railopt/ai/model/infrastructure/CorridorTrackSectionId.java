package com.railopt.ai.model.infrastructure;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Composite primary key for table `corridor_track_sections`. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CorridorTrackSectionId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "corridor_id", nullable = false, columnDefinition = "uuid")
    private UUID corridorId;

    @Column(name = "track_section_id", nullable = false, columnDefinition = "uuid")
    private UUID trackSectionId;

}
