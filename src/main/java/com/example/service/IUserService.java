package com.example.service;


import com.example.entity.User;
import com.example.request.UserCreationRequest;
import com.example.response.AuthResponse;
import com.example.response.UserResponse;
import jakarta.mail.MessagingException;
import org.springframework.security.core.Authentication;

public interface IUserService {
    public AuthResponse createAccount (UserCreationRequest request);

    public AuthResponse login(String email, String password) throws MessagingException;

    public User findUserbyJwt (String jwt);

    public User findUserByEmail(String email);

    public User findUserById(String id);
}
