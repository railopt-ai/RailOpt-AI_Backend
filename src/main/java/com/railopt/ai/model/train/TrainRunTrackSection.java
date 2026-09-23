package com.railopt.ai.model.train;

import java.time.LocalDateTime;

import com.railopt.ai.model.infrastructure.TrackSection;

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


/** Persistence mapping for table `train_run_track_sections`. */
@Entity
@Table(name = "train_run_track_sections")
@Getter
@Setter
@NoArgsConstructor
public class TrainRunTrackSection {

    @EmbeddedId
    private TrainRunTrackSectionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("trainRunId")
    @JoinColumn(name = "train_run_id", nullable = false, columnDefinition = "uuid")
    private TrainRun trainRun;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("trackSectionId")
    @JoinColumn(name = "track_section_id", nullable = false, columnDefinition = "uuid")
    private TrackSection trackSection;

    @Column(name = "sequence_no", columnDefinition = "integer")
    private Integer sequenceNo;

    @Column(name = "planned_entry_at", columnDefinition = "timestamp")
    private LocalDateTime plannedEntryAt;

    @Column(name = "planned_exit_at", columnDefinition = "timestamp")
    private LocalDateTime plannedExitAt;

}
