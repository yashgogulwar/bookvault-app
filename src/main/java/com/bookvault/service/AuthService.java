package com.bookvault.service;

import com.bookvault.dto.request.LoginRequest;
import com.bookvault.dto.response.AuthResponse;
import com.bookvault.repository.AppUserRepository;
import com.bookvault.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final AppUserRepository appUserRepository;

    public AuthResponse login(LoginRequest request) {
        System.out.println("Loaded user: " + request.getEmail());
        System.out.println("Password from DB (should be a BCrypt hash): " + request.getPassword());

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        var user = appUserRepository.findByEmail(request.getEmail()).orElseThrow();


        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        System.out.println(user);
        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
