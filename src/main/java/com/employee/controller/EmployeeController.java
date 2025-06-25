package com.employee.controller;

import com.employee.dto.EmployeeDto;
import com.employee.model.Employee;
import com.employee.exceptionhandling.ApiResponse;
import com.employee.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
  @Operation(summary = "registerUser", description = "saves the users")
  @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "created"),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = " Request understood but authorization denied."),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found ", content = @Content(schema = @Schema(implementation = IllegalStateException.class))),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
  })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<EmployeeDto>> registerEmployeeORUser(@Valid @RequestBody EmployeeDto employeeDto) {
        EmployeeDto createdEmployee = employeeService.registerUser(employeeDto);
        return ResponseEntity.ok(ApiResponse.success(createdEmployee));
    }
    @Operation(summary = "getAll", description = "get all the users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "fetching the user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = " Request understood but authorization denied."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found ", content = @Content(schema = @Schema(implementation = IllegalStateException.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getAll")
     @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<List<Employee>>> getAll() {
        ApiResponse<List<Employee>> response = employeeService.getAll();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @Operation(summary = "getByid", description = "get by the user")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "get by id the user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = " Request understood but authorization denied."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found ", content = @Content(schema = @Schema(implementation = IllegalStateException.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getById")
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    public ResponseEntity<ApiResponse<Employee>> getById(@RequestParam Long id, Authentication authentication) {
        String currentUsername = authentication.getName();

        Optional<Employee> currentUserEmployee = employeeService.getEmployeeByUserName(currentUsername);
        if (currentUserEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("User not linked to any employee record", "401"));
        }

        boolean isAdmin= authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if(!isAdmin && !currentUserEmployee.get().getId().equals(id)){
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Access denied: cannot view other user's data", "403"));
        }
        Optional<Employee> requestedEmployee = employeeService.getById(id);
        if (requestedEmployee.isPresent()) {
            return ResponseEntity.ok(ApiResponse.success(requestedEmployee.get()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("Employee not found with ID: " + id, "404"));
        }
    }
    @Operation(summary = "updateUser", description = "update the users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "update the user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = " Request understood but authorization denied."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found ", content = @Content(schema = @Schema(implementation = IllegalStateException.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ApiResponse<Employee>> updateEmployee(
            @RequestParam Long id,
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
    @Operation(summary = "deleteUser", description = "delete the users")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "delete user"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = " Request understood but authorization denied."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Not Found ", content = @Content(schema = @Schema(implementation = IllegalStateException.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

        @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        public ResponseEntity<ApiResponse<String>> deleteEmployee (@RequestParam Long id){
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

