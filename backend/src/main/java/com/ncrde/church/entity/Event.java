package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events", indexes = {
    @Index(name = "idx_events_event_date", columnList = "event_date"),
    @Index(name = "idx_events_type", columnList = "event_type")
})
@Getter
@Setter
@NoArgsConstructor
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(name = "event_date", nullable = false)
    private LocalDate eventDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(length = 255)
    private String location;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType; // SERVICE, MEETING, YOUTH_PROGRAM, CONFERENCE, RETREAT

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "recurrence_rule_id")
    private RecurrenceRule recurrenceRule;

    @Column(name = "capacity")
    private Integer capacity; // null means unlimited

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_id", nullable = false)
    private Campus campus;
}
