package com.ncrde.church.dto;

import com.ncrde.church.entity.FamilyRole;
import com.ncrde.church.entity.MemberCategory;
import com.ncrde.church.entity.MemberStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class MemberCreateDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    private String email;

    private String phone;
    private String gender;
    private LocalDate dob;
    private String address;

    @NotNull
    private MemberStatus status = MemberStatus.ACTIVE;

    @NotNull
    private MemberCategory category = MemberCategory.ADULT;

    private LocalDate joinedDate = LocalDate.now();

    private Long familyId;
    private FamilyRole familyRole;

    @NotBlank
    private String campusCode = "REMERA";
}
