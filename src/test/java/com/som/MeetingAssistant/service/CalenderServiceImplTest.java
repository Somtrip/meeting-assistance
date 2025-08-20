package com.som.MeetingAssistant.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.som.MeetingAssistant.dto.MeetingRequest;
import com.som.MeetingAssistant.dto.MeetingResponse;
import com.som.MeetingAssistant.entity.Employee;
import com.som.MeetingAssistant.entity.Meeting;
import com.som.MeetingAssistant.repository.EmployeeRepository;
import com.som.MeetingAssistant.repository.MeetingRepository;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CalendarServiceImplTest {

    EmployeeRepository employeeRepository;
    MeetingRepository meetingRepository;
    CalendarService service;

    UUID orgId = UUID.randomUUID();
    UUID p1 = UUID.randomUUID();
    UUID p2 = UUID.randomUUID();

    @BeforeEach
    void setup() {
        employeeRepository = mock(EmployeeRepository.class);
        meetingRepository = mock(MeetingRepository.class);
        service = new CalendarServiceImpl(employeeRepository, meetingRepository);

        when(employeeRepository.findById(orgId))
                .thenReturn(Optional.of(Employee.builder().id(orgId).name("Org").email("org@x.com").build()));
        when(employeeRepository.findById(p1))
                .thenReturn(Optional.of(Employee.builder().id(p1).name("A").email("a@x.com").build()));
        when(employeeRepository.findById(p2))
                .thenReturn(Optional.of(Employee.builder().id(p2).name("B").email("b@x.com").build()));
    }

    @Test
    void bookMeeting_noConflicts_success() {
        LocalDateTime s = LocalDateTime.now().plusHours(1);
        LocalDateTime e = s.plusMinutes(30);
        when(meetingRepository.findOverlappingForParticipant(eq(p1), any(), any()))
                .thenReturn(Collections.emptyList());
        when(meetingRepository.findOverlappingForParticipant(eq(p2), any(), any()))
                .thenReturn(Collections.emptyList());
        when(meetingRepository.save(any(Meeting.class))).thenAnswer(inv -> {
            Meeting m = inv.getArgument(0);
            m.setId(UUID.randomUUID());
            return m;
        });

        MeetingRequest req = MeetingRequest.builder()
                .title("Standup").organizerId(orgId).start(s).end(e)
                .participantIds(List.of(p1, p2)).build();

        MeetingResponse res = service.bookMeeting(req);
        assertNotNull(res.getMeetingId());
        assertEquals("BOOKED", res.getStatus());
        verify(meetingRepository, times(1)).save(any());
    }

    @Test
    void bookMeeting_conflict_throws() {
        LocalDateTime s = LocalDateTime.now().plusHours(1);
        LocalDateTime e = s.plusMinutes(30);
        when(meetingRepository.findOverlappingForParticipant(eq(p1), any(), any()))
                .thenReturn(List.of(new Meeting()));

        MeetingRequest req = MeetingRequest.builder()
                .title("Standup").organizerId(orgId).start(s).end(e)
                .participantIds(List.of(p1)).build();

        assertThrows(IllegalStateException.class, () -> service.bookMeeting(req));
        verify(meetingRepository, never()).save(any());
    }
}

