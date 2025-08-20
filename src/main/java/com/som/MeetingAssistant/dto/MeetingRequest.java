package com.som.MeetingAssistant.dto;


import com.som.MeetingAssistant.validation.ValidTimeRange;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@ValidTimeRange(startField = "start", endField = "end")
public class MeetingRequest {
    @NotBlank private String title;
    @NotNull private UUID organizerId;
    @NotNull private LocalDateTime start;
    @NotNull private LocalDateTime end;
    @NotNull @Size(min = 1, message = "At least one participant required")
    private List<UUID> participantIds;
}

