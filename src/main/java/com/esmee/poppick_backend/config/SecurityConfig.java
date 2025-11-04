package com.esmee.poppick_backend.config;

import com.esmee.poppick_backend.security.JwtAuthenticationFilter;
import com.esmee.poppick_backend.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration

public class SecurityConfig {

    // Authenticatie
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
    }
    //controleert het wachtwoord gegeven vanuit AuthenticationManager.
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);//regelt de matches(raw, hashed)
        return provider;
    }
    //ontvangt log in poging
    @Bean
    public AuthenticationManager authenticationManager(DaoAuthenticationProvider provider) {
        return new ProviderManager(provider);
    }

    // Authorisatie
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider provider, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .cors(cors -> {}) // <-- deze regel toevoegen!
                .csrf(csrf -> csrf.disable())
                .authenticationProvider(provider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // homepage + login/register vrij
                        .requestMatchers("/api/movies/**", "/api/movielists/**", "/api/recommendations/**").hasAnyAuthority("QUIZTAKER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

//regelsysteem van Spring Security
// configureert hoe Spring Security moet werken en UDSI levert alleen de data
//!!! 403 komt door /auth/**
//.anyRequest().permitAll()  alles toegang voor iedereen
//.requestMatchers("/", "/auth/**").permitAll()   home pagina en login pagina toegankelijk voor iedereen
//.requestMatchers("/quiz/**", "/lists/**", "/profile/**").hasAuthority("QUIZTAKER")    alleen QUIZTAKER mag erbij
//.anyRequest().authenticated()    alle andere endpoints vereisen een login
// of denyAll() om toegang te weigeren. Docent wilt deze zien.
//@EnableWebSecurity + .anyRequest().permitAll()
