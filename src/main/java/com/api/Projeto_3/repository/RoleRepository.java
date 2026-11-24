package com.api.Projeto_3.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.api.Projeto_3.model.RolesModel;

@Repository
public interface RoleRepository extends JpaRepository<RolesModel, Long> {

    Optional<RolesModel> findFirstByAuthorityOrderByIdAsc(String authority);

    boolean existsByAuthority(String authority);

    @Modifying
    @Query(value = "LOCK TABLE roles_tb IN SHARE ROW EXCLUSIVE MODE", nativeQuery = true)
    void bloquearCriacaoDeRoles();
}
