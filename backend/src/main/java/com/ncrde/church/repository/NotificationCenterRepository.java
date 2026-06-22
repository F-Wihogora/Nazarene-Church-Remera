package com.ncrde.church.repository;

import com.ncrde.church.entity.NotificationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationCenterRepository extends JpaRepository<NotificationCenter, Long> {
    List<NotificationCenter> findByRecipientIdAndReadStatusFalse(Long recipientId);
}
