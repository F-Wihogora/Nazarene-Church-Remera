package com.ncrde.church.repository;

import com.ncrde.church.entity.Testimony;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestimonyRepository extends JpaRepository<Testimony, Long> {
    List<Testimony> findByApprovedTrue();
    List<Testimony> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
}
