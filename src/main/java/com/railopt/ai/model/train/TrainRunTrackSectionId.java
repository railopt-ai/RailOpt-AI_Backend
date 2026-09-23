package com.railopt.ai.model.train;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Composite primary key for table `train_run_track_sections`. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TrainRunTrackSectionId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "train_run_id", nullable = false, columnDefinition = "uuid")
    private UUID trainRunId;

    @Column(name = "track_section_id", nullable = false, columnDefinition = "uuid")
    private UUID trackSectionId;

}
