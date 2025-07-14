package com.employee.repository;

import com.employee.dto.ListEmployeeDto;
import com.employee.exceptionhandling.ApiResponse;
import com.employee.model.Employee;
import com.employee.model.PocUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{

    Optional<Employee> findByEmail(String email);

    Optional<Employee> findByMobileNumber(String mobileNumber);

    Optional<Employee> findByFkUserId(Long fkUserId);

    @Query(value = "select e.id,e.name from employee e",nativeQuery = true)
    List<ListEmployeeDto> findByName();
}
