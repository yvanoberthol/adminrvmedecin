package com.yvanscoop.gestmedecins.repositories;

import com.yvanscoop.gestmedecins.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query("select r from Role r where r.role like :role")
    List<Role> findByRole(@Param("role") String role);

    @Query("select r from Role r where r.role = :role")
    Role findRole(String role);
}
