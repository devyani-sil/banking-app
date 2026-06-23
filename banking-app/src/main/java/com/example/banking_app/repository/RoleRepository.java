package com.example.banking_app.repository;

import com.example.banking_app.entity.Role;
import com.example.banking_app.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName name);
}
