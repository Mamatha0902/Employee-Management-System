package com.employee.service;


import com.employee.dto.EmployeeDto;
import com.employee.model.Employee;
import com.employee.model.PocRole;
import com.employee.model.PocUser;
import com.employee.repository.EmployeeRepository;
import com.employee.exceptionhandling.ApiResponse;
import com.employee.repository.RoleRepository;
import com.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

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
    public EmployeeDto registerUser(EmployeeDto employeeDto) {
        PocRole userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        PocUser user = new PocUser();
        user.setUsername(employeeDto.getUsername());
        user.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        user.setRoles(Set.of(userRole));
        PocUser savedUser = userRepository.save(user);

        Optional<Employee> existingByEmail = employeeRepository.findByEmail(employeeDto.getEmail());
        if (existingByEmail.isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        Optional<Employee> existingByMobile = employeeRepository.findByMobileNumber(employeeDto.getMobileNumber());
        if (existingByMobile.isPresent()) {
            throw new RuntimeException("Mobile number already exists");
        }

        // Then create and save Employee with relation
        Employee employee = new Employee();
        employee.setName(employeeDto.getName());
        employee.setEmail(employeeDto.getEmail());
        employee.setMobileNumber(employeeDto.getMobileNumber());
        employee.setFkUserId(user.getId()); // link the user

         employeeRepository.save(employee);

        employeeDto.setId(employee.getId());
        employeeDto.setFkUserId(user.getId());
        employeeDto.setPassword("****");
        employeeDto.setRoles(savedUser.getRoles());
        return employeeDto;
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
