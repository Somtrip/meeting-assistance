package com.som.MeetingAssistant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(indexes = { @Index(name="idx_meeting_start_end", columnList="startTime,endTime") })
public class Meeting {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private Employee organizer;

    @ManyToMany
    @JoinTable(name = "meeting_participants",
       joinColumns = @JoinColumn(name = "meeting_id"),
       inverseJoinColumns = @JoinColumn(name = "employee_id"))
    private Set<Employee> participants = new HashSet<>();
}

