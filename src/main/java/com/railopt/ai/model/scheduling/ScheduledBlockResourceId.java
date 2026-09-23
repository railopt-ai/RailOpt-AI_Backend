package com.railopt.ai.model.scheduling;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Composite primary key for table `scheduled_block_resources`. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScheduledBlockResourceId implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "scheduled_block_id", nullable = false, columnDefinition = "uuid")
    private UUID scheduledBlockId;

    @Column(name = "resource_id", nullable = false, columnDefinition = "uuid")
    private UUID resourceId;

}
