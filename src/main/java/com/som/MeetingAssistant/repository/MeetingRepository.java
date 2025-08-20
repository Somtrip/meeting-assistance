package com.som.MeetingAssistant.repository;


import com.som.MeetingAssistant.entity.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {

    
    @Query("""
           select m from Meeting m join m.participants p
           where p.id = :empId and m.endTime > :start and m.startTime < :end
           """)
    List<Meeting> findOverlappingForParticipant(@Param("empId") UUID empId,
                                                @Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);

    @Query("""
           select m from Meeting m join m.participants p
           where p.id = :empId and m.startTime >= :from and m.endTime <= :to
           order by m.startTime
           """)
    List<Meeting> findAllForParticipantWithin(@Param("empId") UUID empId,
                                              @Param("from") LocalDateTime from,
                                              @Param("to") LocalDateTime to);
}

