package com.esmee.poppick_backend.controller;

import com.esmee.poppick_backend.dto.UserDto;
import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.service.UserService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173")
@Transactional
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDto userDto) {
        try {
            userService.registerUser(userDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body("Error: " + ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody UserDto userDto) {
        try {
            String token = userService.authenticateUser(userDto.getUsername(), userDto.getPassword());
            return ResponseEntity.ok(java.util.Map.of("token", token));
        } catch (org.springframework.security.core.AuthenticationException ax) {
            return ResponseEntity.status(401).body(java.util.Map.of("error", ax.getMessage()));
        }
    }

    @PatchMapping("/users/{id}/profile-image")
    public ResponseEntity<User> updateProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {

        User updatedUser = userService.updateProfileImage(id, file);
        return ResponseEntity.ok(updatedUser);
    }
}
    @DeleteMapping("/users/{id}/profile-image")
    public ResponseEntity<Void> deleteProfileImage(@PathVariable Long id, Principal principal) {
        User user = userService.findById(id);

        // Alleen eigenaar mag verwijderen
        if (!user.getUsername().equals(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        userService.deleteProfileImage(id);
        return ResponseEntity.noContent().build();
    }

