package com.example.service;

import com.example.domain.VerificationType;
import com.example.entity.ForgotPasswordToken;
import com.example.entity.User;

public interface IForgotPassword {

    ForgotPasswordToken createToken(
            User user
            , String id
            , String otp
            , VerificationType verificationType
            , String sendTo);


    ForgotPasswordToken findById(String id);

    ForgotPasswordToken findByUser(Long userId);

    void deleteToken(ForgotPasswordToken token);
}
