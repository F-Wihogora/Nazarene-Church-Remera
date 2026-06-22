package com.ncrde.church.repository;

import com.ncrde.church.entity.StreamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StreamSessionRepository extends JpaRepository<StreamSession, Long> {
    List<StreamSession> findByLiveTrue();
}
