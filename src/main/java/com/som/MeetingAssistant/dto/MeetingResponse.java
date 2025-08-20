package com.som.MeetingAssistant.dto;

import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MeetingResponse {
    private UUID meetingId;
    private String status;
}

