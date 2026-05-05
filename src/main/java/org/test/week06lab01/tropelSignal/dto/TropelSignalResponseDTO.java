package org.test.week06lab01.tropelSignal.dto;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
@Setter
@Getter
public class TropelSignalResponseDTO {
    private Long id;
    private Long tropelId;
    private String tropelName;
    private Long guardianId;
    private String guardianName;
    private String senderTag;
    private String rawContent;
    private String signalType;
    private String severity;
    private String assignedUnit;
    private String recommendedAction;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;


}
