package com.ncrde.church.controller;

import com.ncrde.church.entity.*;
import com.ncrde.church.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'SECRETARY', 'TREASURER')")
public class AdminFeaturesController {

    @Autowired
    private PrayerRequestRepository prayerRequestRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private VolunteerAssignmentRepository volunteerAssignmentRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FinanceRecordRepository financeRecordRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CampusRepository campusRepository;

    // --- PRAYER REQUESTS ---
    @GetMapping("/prayers")
    public ResponseEntity<List<PrayerRequest>> getPrayers() {
        return ResponseEntity.ok(prayerRequestRepository.findAll());
    }

    @PostMapping("/prayers")
    public ResponseEntity<PrayerRequest> createPrayer(@RequestBody PrayerRequest request) {
        if (request.getCreatedAt() == null) {
            request.setCreatedAt(java.time.LocalDateTime.now());
        }
        if (request.getStatus() == null) {
            request.setStatus(PrayerRequestStatus.PENDING);
        }
        return ResponseEntity.ok(prayerRequestRepository.save(request));
    }

    @PutMapping("/prayers/{id}/status")
    public ResponseEntity<PrayerRequest> updatePrayerStatus(@PathVariable Long id, @RequestParam PrayerRequestStatus status) {
        PrayerRequest pr = prayerRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prayer request not found"));
        pr.setStatus(status);
        return ResponseEntity.ok(prayerRequestRepository.save(pr));
    }

    // --- VISITORS ---
    @GetMapping("/visitors")
    public ResponseEntity<List<Visitor>> getVisitors() {
        return ResponseEntity.ok(visitorRepository.findAll());
    }

    @PutMapping("/visitors/{id}/followup")
    public ResponseEntity<Visitor> updateVisitorFollowUp(@PathVariable Long id, @RequestParam String status) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));
        visitor.setFollowUpStatus(status);
        return ResponseEntity.ok(visitorRepository.save(visitor));
    }

    @PostMapping("/visitors/{id}/convert")
    @Transactional
    public ResponseEntity<Member> convertVisitorToMember(@PathVariable Long id) {
        Visitor visitor = visitorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visitor not found"));

        Campus campus = visitor.getCampus();
        if (campus == null) {
            campus = campusRepository.findAll().get(0);
        }

        // Create new Member
        Member member = new Member();
        member.setFirstName(visitor.getFirstName());
        member.setLastName(visitor.getLastName());
        member.setEmail(visitor.getEmail());
        member.setPhone(visitor.getPhone());
        member.setGender("UNKNOWN");
        member.setCampus(campus);
        member.setStatus(MemberStatus.ACTIVE);
        member.setCategory(MemberCategory.ADULT);
        member.setJoinedDate(LocalDate.now());
        
        Member savedMember = memberRepository.save(member);

        // Update visitor status
        visitor.setFollowUpStatus("CONVERTED");
        visitorRepository.save(visitor);

        return ResponseEntity.ok(savedMember);
    }

    // --- DEPARTMENTS ---
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(departmentRepository.findAll());
    }

    // --- VOLUNTEER ASSIGNMENTS ---
    @GetMapping("/volunteers")
    public ResponseEntity<List<VolunteerAssignment>> getVolunteers() {
        return ResponseEntity.ok(volunteerAssignmentRepository.findAll());
    }

    @PostMapping("/volunteers")
    @Transactional
    public ResponseEntity<VolunteerAssignment> assignVolunteer(
            @RequestParam Long memberId,
            @RequestParam Long departmentId,
            @RequestParam String roleDescription,
            @RequestParam String date,
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Department dept = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        VolunteerAssignment assignment = new VolunteerAssignment();
        assignment.setMember(member);
        assignment.setDepartment(dept);
        assignment.setRoleDescription(roleDescription);
        assignment.setScheduledDate(LocalDate.parse(date));
        
        if (startTime != null && !startTime.isBlank()) {
            assignment.setStartTime(LocalTime.parse(startTime));
        }
        if (endTime != null && !endTime.isBlank()) {
            assignment.setEndTime(LocalTime.parse(endTime));
        }
        assignment.setStatus("PENDING");

        return ResponseEntity.ok(volunteerAssignmentRepository.save(assignment));
    }

    @DeleteMapping("/volunteers/{id}")
    public ResponseEntity<Void> deleteVolunteerAssignment(@PathVariable Long id) {
        volunteerAssignmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    // --- COURSES (NDI) ---
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseRepository.findAll());
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseRepository.save(course));
    }

    // --- FINANCES ---
    @GetMapping("/finances")
    public ResponseEntity<List<FinanceRecord>> getFinances() {
        return ResponseEntity.ok(financeRecordRepository.findAll());
    }

    @PostMapping("/finances")
    @Transactional
    public ResponseEntity<FinanceRecord> createFinanceRecord(
            @RequestParam String recordType,
            @RequestParam String category,
            @RequestParam double amount,
            @RequestParam String date,
            @RequestParam(required = false) Long memberId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String paymentMethod) {

        FinanceRecord fr = new FinanceRecord();
        fr.setRecordType(recordType);
        fr.setCategory(category);
        fr.setAmount(BigDecimal.valueOf(amount));
        fr.setRecordDate(LocalDate.parse(date));
        
        if (memberId != null) {
            Member member = memberRepository.findById(memberId).orElse(null);
            fr.setMember(member);
        }
        
        fr.setDescription(description);
        fr.setPaymentMethod(paymentMethod != null ? paymentMethod : "CASH");

        return ResponseEntity.ok(financeRecordRepository.save(fr));
    }
}
