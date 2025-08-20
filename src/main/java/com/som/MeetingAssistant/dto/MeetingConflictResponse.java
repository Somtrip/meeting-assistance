package com.som.MeetingAssistant.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MeetingConflictResponse {
    private List<UUID> conflictedParticipantIds;
}

