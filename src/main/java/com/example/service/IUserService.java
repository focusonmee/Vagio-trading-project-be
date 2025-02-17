package com.example.service;


import com.example.domain.VerificationType;
import com.example.entity.User;
import com.example.request.UserCreationRequest;
import com.example.response.AuthResponse;
import jakarta.mail.MessagingException;

public interface IUserService {
    public AuthResponse createAccount(UserCreationRequest request);

    public AuthResponse login(String email, String password) throws MessagingException;

    public User findUserbyJwt(String jwt);

    public User findUserByEmail(String email);

    public User findUserById(Long id);

    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);

    User updatePassword(User user, String newPassword);

    AuthResponse verifyLoginOtp(String otp, String id);

    String sendVerificationOtp(String jwt, VerificationType verificationType) throws MessagingException;


}