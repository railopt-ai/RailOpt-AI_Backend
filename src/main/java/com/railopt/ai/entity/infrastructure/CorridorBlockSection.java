package com.railopt.ai.entity.infrastructure;

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

/** Persistence mapping for table `corridor_block_sections`. */
@Entity
@Table(name = "corridor_block_sections")
@Getter
@Setter
@NoArgsConstructor
public class CorridorBlockSection {

    @EmbeddedId
    private CorridorBlockSectionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("corridorId")
    @JoinColumn(name = "corridor_id", nullable = false, columnDefinition = "uuid")
    private Corridor corridor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("blockSectionId")
    @JoinColumn(name = "block_section_id", nullable = false, columnDefinition = "uuid")
    private BlockSection blockSection;

    @Column(name = "sequence_no", columnDefinition = "integer")
    private Integer sequenceNo;

}
