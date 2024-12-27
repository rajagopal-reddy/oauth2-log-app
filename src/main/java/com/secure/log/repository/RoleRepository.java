package com.secure.log.repository;

import com.secure.log.enums.AppRole;
import com.secure.log.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(AppRole appRole);
}