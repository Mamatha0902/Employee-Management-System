package com.employee.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/emp")
public class Controller {

    @GetMapping
    public ResponseEntity<String> helloWorld(){
        return new ResponseEntity<>("Hello World !", HttpStatus.ACCEPTED);
    }
}
