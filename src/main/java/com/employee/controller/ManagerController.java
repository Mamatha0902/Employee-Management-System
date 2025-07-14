package com.employee.controller;

import com.employee.dto.TaskDto;
import com.employee.feignClient.TaskSeviceClient;
import com.employee.model.Employee;
import com.employee.model.Task;
import com.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/manager")
public class ManagerController {

    @Autowired
    private TaskSeviceClient taskSeviceClient;
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Task>  createTasks(@RequestBody Task task) {

        return ResponseEntity.ok(taskSeviceClient.createTasks(task));
    }
    @PutMapping("/update")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public  ResponseEntity<Task> updateTask(@RequestParam Long id, @RequestBody Task task){
        return ResponseEntity.ok(taskSeviceClient.updateTaskByEmpId(id, task));
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")// Final path: /emp/employee
    public ResponseEntity<List<TaskDto>> getTasksForEmployee(@RequestParam("id") Long employeeId) {
        return ResponseEntity.ok(taskSeviceClient.getTasksByEmployeeId(employeeId));
    }

    @GetMapping("/taskGetAll")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskSeviceClient.getAllTasks());
    }

    @PostMapping("/taskassign")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<String> assignTask(@RequestParam Long empId, @RequestParam Long taskId){
        Employee employee = employeeRepository.findById(empId).orElseThrow(()-> new RuntimeException("Employee Id not found"));
        return ResponseEntity.ok(taskSeviceClient.assignTasks(employee.getId(),taskId));
    }

    @GetMapping("/taskGetById")
    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    public ResponseEntity<Task> getTaskByTaskId(@RequestParam Long taskId){
        return ResponseEntity.ok(taskSeviceClient.getTaskByTaskId(taskId));
    }

}
