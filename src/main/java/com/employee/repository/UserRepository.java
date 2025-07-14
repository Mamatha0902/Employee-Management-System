package com.employee.repository;
import com.employee.model.PocUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<PocUser, Long>

{
    boolean existsByUsername(String username);
    boolean existsByPassword(String password);
    Optional<PocUser> findByUsername(String username);

}
