package com.som.MeetingAssistant.service;


import com.som.MeetingAssistant.dto.*;
import com.som.MeetingAssistant.entity.Employee;
import com.som.MeetingAssistant.entity.Meeting;
import com.som.MeetingAssistant.repository.EmployeeRepository;
import com.som.MeetingAssistant.repository.MeetingRepository;
import com.som.MeetingAssistant.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
    private final EmployeeRepository employeeRepository;
    private final MeetingRepository meetingRepository;

    @Override
    @Transactional
    public MeetingResponse bookMeeting(MeetingRequest request) {
        Employee organizer = employeeRepository.findById(request.getOrganizerId())
                .orElseThrow(() -> new IllegalArgumentException("Organizer not found"));

        Set<Employee> participants = request.getParticipantIds().stream()
                .map(id -> employeeRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Participant not found: " + id)))
                .collect(Collectors.toSet());

        // Conflict check
        for (Employee p : participants) {
            boolean conflict = !meetingRepository.findOverlappingForParticipant(
                    p.getId(), request.getStart(), request.getEnd()).isEmpty();
            if (conflict) {
                throw new IllegalStateException("Participant has a conflicting meeting: " + p.getId());
            }
        }

        Meeting meeting = Meeting.builder()
                .title(request.getTitle())
                .startTime(request.getStart())
                .endTime(request.getEnd())
                .organizer(organizer)
                .participants(participants)
                .build();
        meetingRepository.save(meeting);

        return MeetingResponse.builder()
                .meetingId(meeting.getId())
                .status("BOOKED")
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AvailableSlot> findAvailableSlots(String employee1Id, String employee2Id, int durationInMinutes) {
        UUID e1 = UUID.fromString(employee1Id);
        UUID e2 = UUID.fromString(employee2Id);

        ZoneId zone = ZoneId.systemDefault();
        List<LocalDate> days = TimeUtils.nextBusinessDays(5, zone);

        List<AvailableSlot> common = new ArrayList<>();
        for (LocalDate d : days) {
            LocalDateTime dayStart = TimeUtils.atTime(d, 9, 0);
            LocalDateTime dayEnd   = TimeUtils.atTime(d, 17, 0);

            List<AvailableSlot> e1Free = freeIntervalsFor(e1, dayStart, dayEnd);
            List<AvailableSlot> e2Free = freeIntervalsFor(e2, dayStart, dayEnd);

            
            for (AvailableSlot a : e1Free) {
                for (AvailableSlot b : e2Free) {
                    LocalDateTime s = a.getStart().isAfter(b.getStart()) ? a.getStart() : b.getStart();
                    LocalDateTime e = a.getEnd().isBefore(b.getEnd()) ? a.getEnd() : b.getEnd();
                    if (!e.isBefore(s) && Duration.between(s, e).toMinutes() >= durationInMinutes) {
                        common.add(AvailableSlot.builder().start(s).end(e).build());
                    }
                }
            }
        }
        return mergeSlots(common);
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingConflictResponse findMeetingConflicts(MeetingRequest request) {
        List<UUID> conflicted = new ArrayList<>();
        for (UUID pid : request.getParticipantIds()) {
            List<Meeting> overlaps = meetingRepository
                    .findOverlappingForParticipant(pid, request.getStart(), request.getEnd());
            if (!overlaps.isEmpty()) conflicted.add(pid);
        }
        return MeetingConflictResponse.builder().conflictedParticipantIds(conflicted).build();
    }

   
    private List<AvailableSlot> freeIntervalsFor(UUID employeeId, LocalDateTime from, LocalDateTime to) {
        List<Meeting> meetings = meetingRepository.findAllForParticipantWithin(employeeId, from, to);
        meetings.sort(Comparator.comparing(Meeting::getStartTime));

        List<AvailableSlot> free = new ArrayList<>();
        LocalDateTime cursor = from;
        for (Meeting m : meetings) {
            LocalDateTime ms = m.getStartTime();
            LocalDateTime me = m.getEndTime();
            if (ms.isAfter(cursor)) {
                free.add(AvailableSlot.builder().start(cursor).end(ms).build());
            }
            if (me.isAfter(cursor)) cursor = me;
        }
        if (cursor.isBefore(to)) {
            free.add(AvailableSlot.builder().start(cursor).end(to).build());
        }
        return free;
    }

    private List<AvailableSlot> mergeSlots(List<AvailableSlot> slots) {
        if (slots.isEmpty()) return slots;
        slots.sort(Comparator.comparing(AvailableSlot::getStart).thenComparing(AvailableSlot::getEnd));
        List<AvailableSlot> out = new ArrayList<>();
        AvailableSlot cur = slots.get(0);
        for (int i = 1; i < slots.size(); i++) {
            AvailableSlot next = slots.get(i);
            if (!next.getStart().isAfter(cur.getEnd())) {
                LocalDateTime newEnd = next.getEnd().isAfter(cur.getEnd()) ? next.getEnd() : cur.getEnd();
                cur = AvailableSlot.builder().start(cur.getStart()).end(newEnd).build();
            } else {
                out.add(cur);
                cur = next;
            }
        }
        out.add(cur);
        return out;
    }
}

