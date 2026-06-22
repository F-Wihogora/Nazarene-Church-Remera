package com.ncrde.church.dto;

import com.ncrde.church.entity.AuditLog;
import com.ncrde.church.entity.FinanceRecord;
import com.ncrde.church.entity.Member;
import com.ncrde.church.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DatabaseBackupDto {
    private List<User> users = new ArrayList<>();
    private List<Member> members = new ArrayList<>();
    private List<FinanceRecord> finances = new ArrayList<>();
    private List<AuditLog> auditLogs = new ArrayList<>();
}
