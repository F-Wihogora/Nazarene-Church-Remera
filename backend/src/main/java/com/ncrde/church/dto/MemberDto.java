package com.ncrde.church.dto;

import com.ncrde.church.entity.FamilyRole;
import com.ncrde.church.entity.MemberCategory;
import com.ncrde.church.entity.MemberStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class MemberDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String gender;
    private LocalDate dob;
    private String address;
    private MemberStatus status;
    private MemberCategory category;
    private LocalDate joinedDate;
    
    // User details
    private String username;
    private Long userId;

    // Family details
    private Long familyId;
    private String familyName;
    private FamilyRole familyRole;

    // Campus details
    private String campusCode;
    private String campusName;
}
