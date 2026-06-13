package com.example.bankcards.service;

import com.example.bankcards.dto.request.LoginRequest;
import com.example.bankcards.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
}
