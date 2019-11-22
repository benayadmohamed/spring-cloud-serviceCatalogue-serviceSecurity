package com.repositories;

import com.entities.Roles;
import com.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Roles, Long> {

    Roles findRolesByRole(String role);
}
