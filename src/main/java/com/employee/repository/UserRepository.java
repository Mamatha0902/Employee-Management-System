package com.employee.repository;
import com.employee.model.PocUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<PocUser, Long> {
    PocUser findByUsername(String username);
}
