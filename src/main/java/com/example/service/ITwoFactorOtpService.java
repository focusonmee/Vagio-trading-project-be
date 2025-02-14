package com.example.service;

import com.example.entity.TwoFactorOTP;
import com.example.entity.User;

public interface ITwoFactorOtpService {
        TwoFactorOTP createTwoFactorOTP (User user, String otp, String jwt);
        TwoFactorOTP findbyUser(Long userId);
        TwoFactorOTP findbyId(String id);
        boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOTP,String otp);
        void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
