package com.employee.model;
import jakarta.persistence.*;

@Entity
public class PocRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public PocRole() {}

    public PocRole(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}