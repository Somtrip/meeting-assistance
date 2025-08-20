package com.som.MeetingAssistant.dto;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AvailableSlotsResponse {
    private List<AvailableSlot> slots;
}

