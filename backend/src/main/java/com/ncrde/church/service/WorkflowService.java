package com.ncrde.church.service;

import com.ncrde.church.entity.*;
import com.ncrde.church.repository.AuditLogRepository;
import com.ncrde.church.repository.WorkflowRequestRepository;
import com.ncrde.church.repository.WorkflowStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class WorkflowService {

    @Autowired
    private WorkflowRequestRepository requestRepository;

    @Autowired
    private WorkflowStepRepository stepRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Transactional
    public WorkflowRequest createRequest(String type, String title, String description, Long referenceId) {
        WorkflowRequest request = new WorkflowRequest();
        request.setRequestType(type);
        request.setTitle(title);
        request.setDescription(description);
        request.setReferenceId(referenceId);
        request.setStatus(WorkflowStatus.DRAFT);
        request.setCurrentPendingRole(null);

        WorkflowRequest saved = requestRepository.save(request);
        logStep(saved, "CREATE", "system", "Created request in Draft status");
        return saved;
    }

    @Transactional
    public WorkflowRequest submitRequest(Long requestId) {
        WorkflowRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Workflow request not found: " + requestId));

        if (request.getStatus() != WorkflowStatus.DRAFT && request.getStatus() != WorkflowStatus.REJECTED) {
            throw new RuntimeException("Cannot submit request from current state: " + request.getStatus());
        }

        // Auto-routing logic based on type
        request.setStatus(WorkflowStatus.SUBMITTED);
        
        if ("EXPENSE_APPROVAL".equalsIgnoreCase(request.getRequestType())) {
            // Expenses go to Treasurer first
            request.setStatus(WorkflowStatus.PENDING_TREASURER);
            request.setCurrentPendingRole(UserRoleType.ROLE_TREASURER);
        } else if ("RESERVATION_APPROVAL".equalsIgnoreCase(request.getRequestType())) {
            // Reservations go to Secretary or Pastor. Let's send to Pastor
            request.setStatus(WorkflowStatus.PENDING_PASTOR);
            request.setCurrentPendingRole(UserRoleType.ROLE_PASTOR);
        } else {
            // Default to Pastor approval
            request.setStatus(WorkflowStatus.PENDING_PASTOR);
            request.setCurrentPendingRole(UserRoleType.ROLE_PASTOR);
        }

        WorkflowRequest saved = requestRepository.save(request);
        logStep(saved, "SUBMIT", "user", "Submitted request. Routed to: " + request.getCurrentPendingRole());
        return saved;
    }

    @Transactional
    public WorkflowRequest approveStep(Long requestId, String comments, String username) {
        WorkflowRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Workflow request not found: " + requestId));

        WorkflowStatus currentStatus = request.getStatus();

        if (currentStatus == WorkflowStatus.PENDING_TREASURER) {
            // Treasurer approved -> route to Pastor for final approval
            request.setStatus(WorkflowStatus.PENDING_PASTOR);
            request.setCurrentPendingRole(UserRoleType.ROLE_PASTOR);
            logStep(request, "TREASURER_APPROVE", username, comments);
        } else if (currentStatus == WorkflowStatus.PENDING_PASTOR) {
            // Pastor approved -> request is now fully APPROVED!
            request.setStatus(WorkflowStatus.APPROVED);
            request.setCurrentPendingRole(null);
            logStep(request, "PASTOR_APPROVE", username, comments);

            // Audit log audit snapshot
            auditLogRepository.save(new AuditLog("APPROVE_WORKFLOW", username, "workflow_requests", request.getId(), 
                    "PENDING_PASTOR", "APPROVED"));
        } else {
            throw new RuntimeException("Cannot approve request in current status: " + currentStatus);
        }

        return requestRepository.save(request);
    }

    @Transactional
    public WorkflowRequest rejectStep(Long requestId, String comments, String username) {
        WorkflowRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Workflow request not found: " + requestId));

        WorkflowStatus currentStatus = request.getStatus();

        if (currentStatus == WorkflowStatus.PENDING_TREASURER || currentStatus == WorkflowStatus.PENDING_PASTOR) {
            request.setStatus(WorkflowStatus.REJECTED);
            request.setCurrentPendingRole(null);
            logStep(request, "REJECT", username, comments);
            
            // Audit log snapshot
            auditLogRepository.save(new AuditLog("REJECT_WORKFLOW", username, "workflow_requests", request.getId(), 
                    currentStatus.name(), "REJECTED"));
        } else {
            throw new RuntimeException("Cannot reject request in current status: " + currentStatus);
        }

        return requestRepository.save(request);
    }

    private void logStep(WorkflowRequest request, String action, String performedBy, String comments) {
        WorkflowStep step = new WorkflowStep();
        step.setWorkflowRequest(request);
        step.setAction(action);
        step.setPerformedBy(performedBy);
        step.setComments(comments);
        step.setActionDate(LocalDateTime.now());
        stepRepository.save(step);
    }
}
