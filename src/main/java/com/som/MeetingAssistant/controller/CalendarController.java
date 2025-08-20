package com.som.MeetingAssistant.controller;


import com.som.MeetingAssistant.dto.*;
import com.som.MeetingAssistant.service.CalendarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CalendarController {

    private final CalendarService calendarService;


    @PostMapping("/meetings/book")
    public ResponseEntity<MeetingResponse> book(@Valid @RequestBody MeetingRequest req) {
        return ResponseEntity.ok(calendarService.bookMeeting(req));
    }

    @GetMapping("/slots")
    public ResponseEntity<AvailableSlotsResponse> slots(
            @RequestParam String employee1Id,
            @RequestParam String employee2Id,
            @RequestParam int durationInMinutes) {
        List<AvailableSlot> slots = calendarService
                .findAvailableSlots(employee1Id, employee2Id, durationInMinutes);
        return ResponseEntity.ok(AvailableSlotsResponse.builder().slots(slots).build());
    }

    
    @PostMapping("/meetings/conflicts")
    public ResponseEntity<MeetingConflictResponse> conflicts(@Valid @RequestBody MeetingRequest req) {
        return ResponseEntity.ok(calendarService.findMeetingConflicts(req));
    }
}

