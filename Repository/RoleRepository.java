package com.LUXURYCLIQ.Repository;

import com.LUXURYCLIQ.entity.Role;
import com.LUXURYCLIQ.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {

    @Query(value = "SELECT * FROM role WHERE role_name = :name", nativeQuery = true)
    Optional<Role> findRoleByName(@Param("name") String name);
}
