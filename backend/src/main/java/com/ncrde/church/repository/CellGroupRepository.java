package com.ncrde.church.repository;

import com.ncrde.church.entity.CellGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CellGroupRepository extends JpaRepository<CellGroup, Long> {
    Optional<CellGroup> findByName(String name);
}
