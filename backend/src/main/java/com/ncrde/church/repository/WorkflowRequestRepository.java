package com.ncrde.church.repository;

import com.ncrde.church.entity.UserRoleType;
import com.ncrde.church.entity.WorkflowRequest;
import com.ncrde.church.entity.WorkflowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRequestRepository extends JpaRepository<WorkflowRequest, Long> {
    List<WorkflowRequest> findByStatus(WorkflowStatus status);
    List<WorkflowRequest> findByCurrentPendingRole(UserRoleType role);
}
