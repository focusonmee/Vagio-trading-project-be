package com.example.service;


import com.example.domain.VerificationType;
import com.example.entity.User;
import com.example.request.ForgotPasswordTokenRequest;
import com.example.request.ResetPasswordRequest;
import com.example.request.UserCreationRequest;
import com.example.response.AuthResponse;
import com.example.response.ResetPasswordResponse;
import jakarta.mail.MessagingException;

public interface IUserService {
    AuthResponse createAccount(UserCreationRequest request);

    AuthResponse login(String email, String password) throws MessagingException;

    User findUserbyJwt(String jwt);

    User findUserByEmail(String email);

    User findUserById(Long id);

    User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user);

    User updatePassword(User user, String newPassword);

    AuthResponse verifyLoginOtp(String otp, String id);

    String sendVerificationOtp(String jwt, VerificationType verificationType) throws MessagingException;

    AuthResponse sendForgotPasswordOtp(ForgotPasswordTokenRequest request) throws MessagingException;

    ResetPasswordResponse resetPassword(String id, String jwt, ResetPasswordRequest request);
}