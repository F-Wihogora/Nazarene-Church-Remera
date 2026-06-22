package com.ncrde.church.repository;

import com.ncrde.church.entity.FinanceRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {
    List<FinanceRecord> findByRecordType(String recordType);
    List<FinanceRecord> findByCategory(String category);
}
