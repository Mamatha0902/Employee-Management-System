package com.employee.repository;

import com.employee.model.PocRole;
import com.fasterxml.jackson.annotation.OptBoolean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<PocRole, Long> {
    Optional<PocRole> findByName(String name);
//    List<PocRole> findByNameIn(Set<String> names);
}
