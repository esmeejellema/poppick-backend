package com.esmee.poppick_backend.service;

import com.esmee.poppick_backend.model.User;
import com.esmee.poppick_backend.repository.UserRepository;

import com.esmee.poppick_backend.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Wordt automatisch aangeroepen door Spring Security wanneer iemand probeert in te loggen met authenticationManager
    // Bouwt UserDetails-object dat Spring Security gebruikt om het ingevoerde wachtwoord te vergelijken met de gehashte waarde en om de rollen te kennen.
    // om het ingevoerde wachtwoord te vergelijken met de gehashte waarde.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("UserDetailsServiceImpl.loadUserByUsername -> " + username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return UserDetailsImpl.build(user);

    }

}
//de methode loadUserByUsername zegt dus eigenlijk; ik vang een username op en de userRepository moet gaan zoeken naar
// een username dat overeenkomt met opgevangen username. en wat het teruggeeft, als het gevonden is, is de username en bijbehorende password en rol
//deze class is verplicht in Spring Security en wordt aangeroepen alleen bij inloggen in de service laag via authenticationManager.
//UDSI geeft object voor data zodat Spring in de repo