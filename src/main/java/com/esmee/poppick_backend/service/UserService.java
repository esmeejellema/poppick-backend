package com.esmee.poppick_backend.service;

import com.esmee.poppick_backend.dto.UserDto;
import com.esmee.poppick_backend.exception.RoleNotFoundException;
import com.esmee.poppick_backend.exception.UserNotFoundException;
import com.esmee.poppick_backend.exception.UsernameAlreadyExistsException;
import com.esmee.poppick_backend.model.Role;
import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.repository.RoleRepository;
import com.esmee.poppick_backend.repository.UserRepository;
import com.esmee.poppick_backend.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//import images
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // Registratie van gebruiker (met standaardrol QUIZTAKER)
    public void registerUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException(
                    "Username '" + userDto.getUsername() + "' is already taken"
            );
        }

        Role quiztakerRole = roleRepository.findByName("QUIZTAKER")
                .orElseThrow(() -> new RoleNotFoundException("Role 'QUIZTAKER' not found"));

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(quiztakerRole);

        userRepository.save(user);
    }

    // Inloggen gebruiker
    public String authenticateUser(String username, String password) {
        System.out.println("[AUTH] attempt username=" + username);
        System.out.println("[DEBUG] Checking user: " + username);
        System.out.println("[DEBUG] Raw password: " + password);


        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            Authentication authentication = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateJwtToken(authentication);
            System.out.println("[AUTH] jwt issued");
            return jwt;

        } catch (AuthenticationException ax) {
            throw ax; // Controller handelt 401 af
        }
    }
        public User updateProfileImage(Long userId, MultipartFile file) throws IOException {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (file == null || file.isEmpty()) {
                throw new RuntimeException("No file provided");
            }

            // Zorg dat uploads folder bestaat
            String uploadDir = "uploads/";
            Files.createDirectories(Paths.get(uploadDir));

            // Unieke bestandsnaam
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir + fileName);

            // Bestand opslaan
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // URL/path opslaan in database
            user.setProfileImage("/uploads/" + fileName);
            return userRepository.save(user);
        }
    public void deleteProfileImage(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Optioneel: bestand verwijderen van filesystem
        if (user.getProfileImage() != null) {
            Path filePath = Paths.get("uploads/" + user.getProfileImage());
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        user.setProfileImage(null);
        userRepository.save(user);
    }

}