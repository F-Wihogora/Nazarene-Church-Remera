package com.ncrde.church.repository;

import com.ncrde.church.entity.Role;
import com.ncrde.church.entity.UserRoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(UserRoleType name);
}
