package com.employee.service;


import com.employee.dto.EmployeeDto;
import com.employee.dto.TaskDto;
import com.employee.feignClient.TaskSeviceClient;
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
    @Autowired
    private TaskSeviceClient taskSeviceClient;

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

    public EmployeeDto registerUser(EmployeeDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (employeeRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        if (employeeRepository.findByMobileNumber(dto.getMobileNumber()).isPresent()) {
            throw new RuntimeException("Mobile number already exists");
        }

        // Create and encode user
        PocUser user = new PocUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // ✅ ENCODE the password
        PocRole defaultRole = roleRepository.findByName("ROLE_USER").orElseThrow(()-> new RuntimeException(" default role not found"));

        user.setRoles(Set.of(defaultRole));



        PocUser savedUser = userRepository.save(user);

        // Create employee
        Employee employee = new Employee();
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setMobileNumber(dto.getMobileNumber());
        employee.setFkUserId(savedUser.getId());

        Employee savedEmployee = employeeRepository.save(employee);

        dto.setId(savedEmployee.getId());
        dto.setFkUserId(savedUser.getId());
        dto.setRoles(savedUser.getRoles());
        return dto;
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

    public Optional<Employee> getEmployeeByUserName(String username) {
        Optional<PocUser> userOpt = userRepository.findByUsername(username);

        if (userOpt.isPresent()) {
            Long userId = userOpt.get().getId();
            return employeeRepository.findByFkUserId(userId);
        }

        return Optional.empty();
    }
    public List<TaskDto> getTasksForEmployee(Long employeeId) {
        return taskSeviceClient.getTasksByEmployeeId(employeeId);
    }
}
