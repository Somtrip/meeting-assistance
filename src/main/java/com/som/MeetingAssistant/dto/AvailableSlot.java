package com.som.MeetingAssistant.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableSlot {
    private LocalDateTime start;
    private LocalDateTime end;
}