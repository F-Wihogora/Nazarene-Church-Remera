package com.ncrde.church.controller;

import com.ncrde.church.dto.MessageResponse;
import com.ncrde.church.service.BackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/backup")
public class BackupController {

    @Autowired
    private BackupService backupService;

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportBackup() {
        String backupData = backupService.exportDatabase();
        byte[] backupBytes = backupData.getBytes();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ncrde_db_backup.json")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(backupBytes);
    }

    @PostMapping("/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> restoreBackup(@RequestBody String backupJson) {
        backupService.restoreDatabase(backupJson);
        return ResponseEntity.ok(new MessageResponse("Database backup restored successfully."));
    }
}
