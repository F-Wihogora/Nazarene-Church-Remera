package com.ncrde.church.repository;

import com.ncrde.church.entity.ServicePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServicePlanRepository extends JpaRepository<ServicePlan, Long> {
    Optional<ServicePlan> findByEventId(Long eventId);
}
