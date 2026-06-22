package com.ncrde.church.controller;

import com.ncrde.church.dto.DashboardAnalyticsDto;
import com.ncrde.church.entity.FinanceRecord;
import com.ncrde.church.entity.MemberStatus;
import com.ncrde.church.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private PrayerRequestRepository prayerRequestRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private FinanceRecordRepository financeRecordRepository;

    @Autowired
    private StreamSessionRepository streamSessionRepository;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'TREASURER', 'SECRETARY')")
    @Cacheable(value = "dashboardStats")
    public ResponseEntity<DashboardAnalyticsDto> getDashboardStats() {
        DashboardAnalyticsDto dto = new DashboardAnalyticsDto();

        dto.setTotalMembers(memberRepository.findAll().stream().filter(m -> !m.isDeleted()).count());
        dto.setActiveMembers(memberRepository.findByStatus(MemberStatus.ACTIVE).stream().filter(m -> !m.isDeleted()).count());
        dto.setTotalVisitors(visitorRepository.count());
        dto.setTotalPrayerRequests(prayerRequestRepository.count());
        dto.setTotalEvents(eventRepository.findAll().stream().filter(e -> !e.isDeleted()).count());
        dto.setActiveStreams(streamSessionRepository.findByLiveTrue().size());

        // Finance calculations
        List<FinanceRecord> records = financeRecordRepository.findAll();
        BigDecimal totalIncome = records.stream()
                .filter(r -> "INCOME".equalsIgnoreCase(r.getRecordType()))
                .map(FinanceRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = records.stream()
                .filter(r -> "EXPENSE".equalsIgnoreCase(r.getRecordType()))
                .map(FinanceRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setTotalIncome(totalIncome);
        dto.setTotalExpenses(totalExpenses);

        return ResponseEntity.ok(dto);
    }
}
