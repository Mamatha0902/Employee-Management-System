package com.employee.repository;

import com.employee.model.PocRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<PocRole, Long> {
    PocRole findByName(String name);
    List<PocRole> findByNameIn(Set<String> names);
}
