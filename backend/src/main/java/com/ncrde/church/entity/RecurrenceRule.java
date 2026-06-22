package com.ncrde.church.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "recurrence_rules")
@Getter
@Setter
@NoArgsConstructor
public class RecurrenceRule extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String frequency; // DAILY, WEEKLY, MONTHLY, CUSTOM

    @Column(name = "recur_interval", nullable = false)
    private Integer recurInterval = 1; // e.g. every 1 week, every 2 months

    @Column(name = "day_of_week_pattern", length = 50)
    private String dayOfWeekPattern; // SUNDAY, FRIDAY, etc.

    @Column(name = "week_of_month_pattern", length = 50)
    private String weekOfMonthPattern; // FIRST, SECOND, THIRD, FOURTH, LAST

    @Column(name = "end_date")
    private LocalDate endDate;
}
