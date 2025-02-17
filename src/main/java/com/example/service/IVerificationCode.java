package com.example.service;

import com.example.domain.VerificationType;
import com.example.entity.User;
import com.example.entity.VerificationCode;

public interface IVerificationCode {
    VerificationCode sendVerificationCode(User user, VerificationType verificationType);

    VerificationCode getVerificationCodebyId(Long id);

    VerificationCode getVerificationCodeByUser(Long userId);

    void deleteVerificationCodeById(VerificationCode verificationCode);


}
