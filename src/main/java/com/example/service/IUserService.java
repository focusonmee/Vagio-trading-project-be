package com.example.service;


import com.example.request.UserCreationRequest;
import com.example.response.AuthResponse;
import com.example.response.UserResponse;
import org.springframework.security.core.Authentication;

public interface IUserService {
    public AuthResponse createAccount (UserCreationRequest request);

    public AuthResponse signIn(String email, String password) ;
}
