package com.example.service;

import com.example.domain.VerificationType;
import com.example.entity.User;
import com.example.entity.VerificationCode;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.repository.VerificationCodeRepository;
import com.example.utils.OtpUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class VerificationCodeService implements IVerificationCode {

    VerificationCodeRepository verificationCodeRepository;


    @Override
    public VerificationCode sendVerificationCode(User user , VerificationType verificationType) {
        VerificationCode verificationCode1 = new VerificationCode();
        verificationCode1.setOtp(OtpUtils.generatedOTP());
        verificationCode1.setVerificationType(verificationType);
        verificationCode1.setUser(user);

        return verificationCodeRepository.save(verificationCode1);
    }

    @Override
    public VerificationCode getVerificationCodebyId(Long id) {
        return verificationCodeRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public VerificationCode getVerificationCodeByUser(Long userId) {
        return verificationCodeRepository.findByUserId(userId);
    }

    @Override
    public void deleteVerificationCodeById(VerificationCode verificationCode) {
        verificationCodeRepository.delete(verificationCode);
    }
}
