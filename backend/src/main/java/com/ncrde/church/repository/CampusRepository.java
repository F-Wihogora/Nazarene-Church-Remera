package com.ncrde.church.repository;

import com.ncrde.church.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CampusRepository extends JpaRepository<Campus, Long> {
    Optional<Campus> findByCode(String code);
    Optional<Campus> findByName(String name);
}
