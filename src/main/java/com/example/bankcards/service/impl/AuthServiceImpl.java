package com.example.bankcards.service.impl;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.response.JwtResponse;
import com.example.bankcards.security.JWTTokenprovider;
import com.example.bankcards.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JWTTokenprovider jwtTokenprovider;

    @Override
    public JwtResponse login(LoginRequest request) {

        System.out.println("SWAGGERDAN KELGAN USERNAME: " + request.username());
        System.out.println("SWAGGERDAN KELGAN PASSWORD: " + request.password());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenprovider.generateToken(authentication);
        return new JwtResponse(jwt, "Bearer");
    }
}
