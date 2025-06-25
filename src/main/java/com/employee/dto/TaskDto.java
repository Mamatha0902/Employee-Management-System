package com.employee.dto;

import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String description;
    private Long employeeId;
}
