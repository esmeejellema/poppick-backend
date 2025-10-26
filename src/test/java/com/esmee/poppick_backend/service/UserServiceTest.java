package com.esmee.poppick_backend.service;

import com.esmee.poppick_backend.dto.UserDto;
import com.esmee.poppick_backend.exception.RoleNotFoundException;
import com.esmee.poppick_backend.exception.UsernameAlreadyExistsException;
import com.esmee.poppick_backend.model.Role;
import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.repository.RoleRepository;
import com.esmee.poppick_backend.repository.UserRepository;
import com.esmee.poppick_backend.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private Authentication authentication;
    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ---------- registerUser() ----------

    @Test
    void registerUser_shouldSaveUser_whenUsernameNotExistsAndRoleFound() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setUsername("newUser");
        dto.setEmail("test@mail.com");
        dto.setPassword("password");

        Role role = new Role();
        role.setName("QUIZTAKER");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(roleRepository.findByName("QUIZTAKER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password")).thenReturn("encodedPass");

        // Act
        userService.registerUser(dto);

        // Assert
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUser_shouldThrowException_whenUsernameAlreadyExists() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setUsername("existingUser");
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        // Act + Assert
        assertThrows(UsernameAlreadyExistsException.class, () -> userService.registerUser(dto));
    }

    @Test
    void registerUser_shouldThrowException_whenRoleNotFound() {
        // Arrange
        UserDto dto = new UserDto();
        dto.setUsername("newUser");
        dto.setPassword("pass");
        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(roleRepository.findByName("QUIZTAKER")).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(RoleNotFoundException.class, () -> userService.registerUser(dto));
    }

    // ---------- authenticateUser() ----------

    @Test
    void authenticateUser_shouldReturnJwt_whenCredentialsValid() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("fake-jwt");

        // Act
        String token = userService.authenticateUser("user", "pass");

        // Assert
        assertEquals("fake-jwt", token);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void authenticateUser_shouldThrowException_whenAuthenticationFails() {
        // Arrange
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Auth failed"));

        // Act + Assert
        assertThrows(RuntimeException.class, () -> userService.authenticateUser("user", "wrong"));
    }
}
