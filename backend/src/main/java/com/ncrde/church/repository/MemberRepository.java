package com.ncrde.church.repository;

import com.ncrde.church.entity.Member;
import com.ncrde.church.entity.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Member> findByStatus(MemberStatus status);
    List<Member> findByFamilyId(Long familyId);
}
