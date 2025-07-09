package com.employee.feignClient;

import com.employee.dto.TaskDto;
import com.employee.model.Task;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "task-service", url = "http://localhost:8081")
public interface TaskSeviceClient {

    @GetMapping("/tasks/employee")
    List<TaskDto> getTasksByEmployeeId(@RequestParam("id") Long employeeId);

    @PostMapping("/tasks/save")
    Task createTasks(@RequestBody Task task);

    @PutMapping("/tasks/update")
    Task updateTaskByEmpId(@RequestParam Long id, @RequestBody Task dto);

    @GetMapping("/tasks/all")
    List<Task> getAllTasks();
}
