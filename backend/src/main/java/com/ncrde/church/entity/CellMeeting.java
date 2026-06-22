package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "cell_meetings", indexes = {
    @Index(name = "idx_cell_meetings_date", columnList = "meeting_date")
})
@Getter
@Setter
@NoArgsConstructor
public class CellMeeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cell_group_id", nullable = false)
    private CellGroup cellGroup;

    @Column(name = "meeting_date", nullable = false)
    private LocalDate meetingDate;

    @Column(name = "attendance_count")
    private Integer attendanceCount = 0;

    @Column(name = "report_summary", length = 1000)
    private String reportSummary;
}
