package com.som.MeetingAssistant.service;

import java.util.List;
import com.som.MeetingAssistant.dto.*;

public interface CalendarService {
    MeetingResponse bookMeeting(MeetingRequest request);
    List<AvailableSlot> findAvailableSlots(String employee1Id, String employee2Id, int durationInMinutes);
    MeetingConflictResponse findMeetingConflicts(MeetingRequest request);
}

