package com.ncrde.church.repository;

import com.ncrde.church.entity.VolunteerAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VolunteerAssignmentRepository extends JpaRepository<VolunteerAssignment, Long> {
    List<VolunteerAssignment> findByScheduledDate(LocalDate date);
    List<VolunteerAssignment> findByMemberId(Long memberId);
    List<VolunteerAssignment> findByDepartmentId(Long departmentId);
}
