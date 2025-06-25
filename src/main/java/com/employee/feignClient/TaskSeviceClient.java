package com.employee.feignClient;

import com.employee.dto.TaskDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "task-service", url = "http://localhost:8081")
public interface TaskSeviceClient {

    @GetMapping("/tasks/employee")
    List<TaskDto> getTasksByEmployeeId(@RequestParam("id") Long employeeId);
}
