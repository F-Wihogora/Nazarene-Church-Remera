package com.ncrde.church.repository;

import com.ncrde.church.entity.Member;
import com.ncrde.church.entity.MemberCategory;
import com.ncrde.church.entity.MemberStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class MemberSpecifications {

    public static Specification<Member> isNotDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleted"), false);
    }

    public static Specification<Member> hasStatus(MemberStatus status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<Member> hasCategory(MemberCategory category) {
        return (root, query, cb) -> category == null ? null : cb.equal(root.get("category"), category);
    }

    public static Specification<Member> hasGender(String gender) {
        return (root, query, cb) -> !StringUtils.hasText(gender) ? null : cb.equal(root.get("gender"), gender);
    }

    public static Specification<Member> hasCampusCode(String campusCode) {
        return (root, query, cb) -> !StringUtils.hasText(campusCode) ? null : cb.equal(root.get("campus").get("code"), campusCode.toUpperCase());
    }

    public static Specification<Member> searchByNameOrEmail(String keyword) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(keyword)) {
                return null;
            }
            String pattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(root.get("firstName")), pattern),
                cb.like(cb.lower(root.get("lastName")), pattern),
                cb.like(cb.lower(root.get("email")), pattern)
            );
        };
    }
}
