package com.esmee.poppick_backend.config;

import com.esmee.poppick_backend.model.Role;
import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.repository.RoleRepository;
import com.esmee.poppick_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository,
                                RoleRepository roleRepository,
                                PasswordEncoder passwordEncoder,
                                @Value("${ADMIN_PASSWORD}") String adminPassword) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {

                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@poppick.com");
                System.out.println("DEBUG >> ADMIN_PASSWORD raw value: '" + adminPassword + "'");
                admin.setPassword(passwordEncoder.encode(adminPassword));
                admin.setRole(adminRole);

                userRepository.save(admin);
                System.out.println("Admin user aangemaakt: username='admin', password='" + adminPassword + "'");
                System.out.println("DEBUG >> Admin opgeslagen in DB: " + admin.getPassword());

            } else {
                System.out.println("Admin user bestaat al, geen nieuwe aangemaakt.");
                System.out.println("DEBUG >> Encoded admin password: " + passwordEncoder.encode(adminPassword));

            }
        };
    }
}
