package com.ncrde.church.repository;

import com.ncrde.church.entity.PrayerRequest;
import com.ncrde.church.entity.PrayerRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrayerRequestRepository extends JpaRepository<PrayerRequest, Long> {
    List<PrayerRequest> findByStatus(PrayerRequestStatus status);
}
