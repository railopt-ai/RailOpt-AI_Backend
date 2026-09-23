package com.railopt.ai.model.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Persistence mapping for table `corridor_track_sections`. */
@Entity
@Table(name = "corridor_track_sections")
@Getter
@Setter
@NoArgsConstructor
public class CorridorTrackSection {

    @EmbeddedId
    private CorridorTrackSectionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("corridorId")
    @JoinColumn(name = "corridor_id", nullable = false, columnDefinition = "uuid")
    private Corridor corridor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("trackSectionId")
    @JoinColumn(name = "track_section_id", nullable = false, columnDefinition = "uuid")
    private TrackSection trackSection;

    @Column(name = "sequence_no", columnDefinition = "integer")
    private Integer sequenceNo;

}
