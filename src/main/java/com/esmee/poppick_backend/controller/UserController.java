package com.esmee.poppick_backend.controller;

import com.esmee.poppick_backend.dto.UserDto;
//import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.service.UserService;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import java.util.HashMap;
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

//    // Voeg deze methode toe aan je bestaande UserController
//    @PutMapping("/users/{id}/profile-image")
//    public ResponseEntity<User> updateProfileImage(
//            @PathVariable Long id,
//            @RequestParam(required = false) String filename) {
//
//        User updatedUser = userService.updateProfileImage(id, filename);
//        return ResponseEntity.ok(updatedUser);
//    }


}
