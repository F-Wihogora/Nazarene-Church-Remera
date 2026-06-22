package com.ncrde.church.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class DashboardAnalyticsDto {
    private long totalMembers;
    private long activeMembers;
    private long totalVisitors;
    private long totalPrayerRequests;
    private long totalEvents;
    private BigDecimal totalIncome;
    private BigDecimal totalExpenses;
    private long activeStreams;
}
