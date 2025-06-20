package com.employee.init;

import com.employee.model.PocRole;
import com.employee.model.PocUser;
import com.employee.repository.RoleRepository;
import com.employee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private RoleRepository roleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (roleRepo.findByName("ROLE_ADMIN") == null) {
            roleRepo.save(new PocRole(null, "ROLE_ADMIN"));
        }
        PocRole userRole = roleRepo.findByName("ROLE_USER").get();
        if (userRole == null) {
            userRole = new PocRole();
            userRole.setName("ROLE_USER");
            userRole = roleRepo.save(userRole); // save and assign back
            System.out.println(" ROLE_USER saved"+ userRole);
        }


        if (userRepo.findByUsername("admin") == null) {
            PocRole adminPocRole = roleRepo.findByName("ROLE_ADMIN").get();
            PocUser admin = new PocUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRoles(Set.of(adminPocRole));
            userRepo.save(admin);
        }
    }
}

