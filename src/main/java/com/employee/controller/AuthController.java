package com.employee.controller;

import com.employee.model.*;
import com.employee.repository.EmployeeRepository;
import com.employee.repository.UserRepository;
import com.employee.service.PocUserService;
import com.employee.utility.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
//@CrossOrigin(origins = "*")
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PocUserService userService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            // This will fail if password in DB is not encoded
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            final UserDetails userDetails = userService.loadUserByUsername(request.getUsername());
            final String jwt = jwtUtil.generateToken(userDetails);
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

            PocUser pocUser = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String role = pocUser.getRoles().stream()
                    .findFirst()
                    .map(PocRole::getName)
                    .orElse("ROLE_UNKNOWN");

            //Fetching employee
            Long employeeId = employeeRepository.findByFkUserId(pocUser.getId())
                    .map(Employee::getId)
                    .orElse(null);

            return ResponseEntity.ok(new AuthResponse(jwt, refreshToken, role, employeeId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token missing");
        }

        try {
            String username = jwtUtil.extractUsernameFromRefresh(refreshToken);
            PocUser pocUser = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            final UserDetails userDetails = userService.loadUserByUsername(username);
            final String newAccessToken = jwtUtil.generateToken(userDetails);

            String role = pocUser.getRoles().stream()
                    .findFirst()
                    .map(PocRole::getName)
                    .orElse("ROLE_UNKNOWN");

            return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken, role, pocUser.getId()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }
}
