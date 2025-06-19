package com.employee.service;


import com.employee.model.Employee;
import com.employee.repository.EmployeeRepository;
import com.employee.exceptionhandling.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee registerEmployee(Employee employee) {
        Optional<Employee> existingByEmail = employeeRepository.findByEmail(employee.getEmail());
        if (existingByEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Optional<Employee> existingByMobile = employeeRepository.findByMobileNumber(employee.getMobileNumber());
        if (existingByMobile.isPresent()) {
            throw new RuntimeException("Mobile number already exists");
        }

        return employeeRepository.save(employee);
    }

    public ApiResponse<List<Employee>> getAll() {
        return ApiResponse.success(employeeRepository.findAll());
    }

    public Optional<Employee> getById(Long id) {
        return employeeRepository.findById(id);
    }

    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void deleteById(Long id) {
        employeeRepository.deleteById(id);
    }
}
