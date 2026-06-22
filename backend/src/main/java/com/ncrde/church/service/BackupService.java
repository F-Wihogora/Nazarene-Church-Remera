package com.ncrde.church.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ncrde.church.dto.DatabaseBackupDto;
import com.ncrde.church.repository.AuditLogRepository;
import com.ncrde.church.repository.FinanceRecordRepository;
import com.ncrde.church.repository.MemberRepository;
import com.ncrde.church.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BackupService {

    private static final Logger logger = LoggerFactory.getLogger(BackupService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FinanceRecordRepository financeRecordRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    private final ObjectMapper objectMapper;

    public BackupService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Support LocalDate/LocalDateTime serialization
    }

    @Transactional(readOnly = true)
    public String exportDatabase() {
        try {
            DatabaseBackupDto backup = new DatabaseBackupDto();
            backup.setUsers(userRepository.findAll());
            // Filter out lazy loaded relations to prevent infinite recursions in JSON
            backup.setMembers(memberRepository.findAll().stream().peek(m -> {
                m.setUser(null);
                m.setFamily(null);
            }).toList());
            backup.setFinances(financeRecordRepository.findAll().stream().peek(f -> f.setMember(null)).toList());
            backup.setAuditLogs(auditLogRepository.findAll());

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(backup);
            logger.info("Database exported successfully to JSON.");
            return json;
        } catch (Exception e) {
            logger.error("Failed to export database", e);
            throw new RuntimeException("Failed to export database backup", e);
        }
    }

    @Transactional
    public void restoreDatabase(String backupJson) {
        try {
            DatabaseBackupDto backup = objectMapper.readValue(backupJson, DatabaseBackupDto.class);
            logger.info("Restoring database from snapshot: Users count: {}, Members count: {}", 
                    backup.getUsers().size(), backup.getMembers().size());

            // Clear existing tables in correct constraint order
            financeRecordRepository.deleteAllInBatch();
            memberRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
            auditLogRepository.deleteAllInBatch();

            // Save records
            userRepository.saveAll(backup.getUsers());
            memberRepository.saveAll(backup.getMembers());
            financeRecordRepository.saveAll(backup.getFinances());
            auditLogRepository.saveAll(backup.getAuditLogs());

            logger.info("Database restoration completed successfully.");
        } catch (Exception e) {
            logger.error("Failed to restore database from backup", e);
            throw new RuntimeException("Failed to restore database from backup", e);
        }
    }
}
