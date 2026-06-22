package com.ncrde.church.controller;

import com.ncrde.church.dto.MessageResponse;
import com.ncrde.church.entity.WorkflowRequest;
import com.ncrde.church.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowController {

    @Autowired
    private WorkflowService workflowService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkflowRequest> createRequest(
            @RequestParam String type,
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long referenceId) {
        
        WorkflowRequest request = workflowService.createRequest(type, title, description, referenceId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<WorkflowRequest> submitRequest(@PathVariable Long id) {
        WorkflowRequest request = workflowService.submitRequest(id);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'TREASURER')")
    public ResponseEntity<WorkflowRequest> approveStep(
            @PathVariable Long id,
            @RequestParam(required = false) String comments) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        WorkflowRequest request = workflowService.approveStep(id, comments, username);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'PASTOR', 'TREASURER')")
    public ResponseEntity<WorkflowRequest> rejectStep(
            @PathVariable Long id,
            @RequestParam(required = false) String comments) {
        
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        WorkflowRequest request = workflowService.rejectStep(id, comments, username);
        return ResponseEntity.ok(request);
    }
}
