package com.employee.controller;

import com.employee.dto.EmployeeDto;
import com.employee.model.Employee;
import com.employee.exceptionhandling.ApiResponse;
import com.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/emp")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

//    @PostMapping("/register")
//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    public ResponseEntity<ApiResponse<Employee>> registerEmployee(@RequestBody Employee employee) {
//        Employee createdEmployee = employeeService.registerEmployee(employee);
//        return ResponseEntity.ok(ApiResponse.success(createdEmployee));
//    }
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<EmployeeDto>> registerEmployeeORUser(@RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.registerUser(employeeDto);
        return ResponseEntity.ok(ApiResponse.success(createdEmployee));
    }

    @GetMapping("/getAll")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Employee>>> getAll() {
        ApiResponse<List<Employee>> response = employeeService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getById")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<ApiResponse<Employee>> getById(@RequestParam Long id) {
        Optional<Employee> employee = employeeService.getById(id);

        if (employee.isPresent()) {
            ApiResponse<Employee> response = ApiResponse.success(employee.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<Employee> response = ApiResponse.error("Employee not found with ID: " + id, "404");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//  @PreAuthorize("('ADMIN')")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee updatedEmployee) {

        Optional<Employee> existingEmployee = employeeService.getById(id);

        if (existingEmployee.isPresent()) {
            Employee employee = existingEmployee.get();
            employee.setName(updatedEmployee.getName());
            employee.setEmail(updatedEmployee.getEmail());
            employee.setMobileNumber(updatedEmployee.getMobileNumber());

            Employee saved = employeeService.saveEmployee(employee);
            return ResponseEntity.ok(ApiResponse.success(saved));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Employee not found with ID: " + id, "404"));
        }
    }

        @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        public ResponseEntity<ApiResponse<String>> deleteEmployee (@PathVariable Long id){
            Optional<Employee> employee = employeeService.getById(id);

            if (employee.isPresent()) {
                employeeService.deleteById(id);
                return ResponseEntity.ok(ApiResponse.success("Employee deleted successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Employee not found with ID: " + id, "404"));
            }
        }
    }

