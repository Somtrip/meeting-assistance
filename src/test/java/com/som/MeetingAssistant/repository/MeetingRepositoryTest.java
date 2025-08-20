package com.som.MeetingAssistant.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.som.MeetingAssistant.entity.Employee;
import com.som.MeetingAssistant.entity.Meeting;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MeetingRepositoryTest {

    @Autowired EmployeeRepository employeeRepository;
    @Autowired MeetingRepository meetingRepository;

    @Test
    void overlappingQuery_works() {
        Employee e = employeeRepository.save(Employee.builder().name("A").email("a@x.com").build());

        LocalDateTime s1 = LocalDateTime.now().plusHours(1);
        LocalDateTime e1 = s1.plusMinutes(60);
        Meeting m1 = Meeting.builder().title("M1").startTime(s1).endTime(e1)
                .organizer(e).participants(Set.of(e)).build();
        meetingRepository.save(m1);


        LocalDateTime qS = s1.plusMinutes(30);
        LocalDateTime qE = e1.plusMinutes(10);
        List<Meeting> res = meetingRepository.findOverlappingForParticipant(e.getId(), qS, qE);
        assertEquals(1, res.size());
        
        res = meetingRepository.findOverlappingForParticipant(e.getId(), e1, e1.plusMinutes(10));
        assertTrue(res.isEmpty());
    }
}

