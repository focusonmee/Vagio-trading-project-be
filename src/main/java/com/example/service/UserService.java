package com.example.service;


import com.example.configuration.JwtProvider;
import com.example.domain.TwoFactorAuth;
import com.example.domain.VerificationType;
import com.example.entity.ForgotPasswordToken;
import com.example.entity.TwoFactorOTP;
import com.example.entity.User;
import com.example.entity.VerificationCode;
import com.example.enums.USER_ROLE;
import com.example.error.ErrorCode;
import com.example.exception.AppException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.request.ForgotPasswordTokenRequest;
import com.example.request.ResetPasswordRequest;
import com.example.request.UserCreationRequest;
import com.example.response.ApiResponse;
import com.example.response.AuthResponse;
import com.example.response.ResetPasswordResponse;
import com.example.response.UserResponse;
import com.example.utils.OtpUtils;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService implements IUserService {

    UserRepository userRepository;
    UserMapper userMapper;
    TwoFactorOtpService twoFactorOtpService;
    EmailService emailService;
    VerificationCode verificationCode;
    VerificationCodeService verificationCodeService;
    ForgotPasswordService forgotPasswordService;

    @Override
    public AuthResponse createAccount(UserCreationRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User newUser = userMapper.toUser(request);
        newUser.setRole(USER_ROLE.ROLE_CUSTOMER);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);
        Authentication auth = new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = JwtProvider.generateToken(auth);

        AuthResponse res = new AuthResponse();
        res.setJwt(jwt);
        res.setStatus(true);
        res.setMessage("register success");
        return res;
    }

    @Override
    public AuthResponse login(String email, String password) throws MessagingException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        boolean authenticated = passwordEncoder.matches(password, user.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.USER_WRONG_PASSWORD);
        }
        Authentication auth = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = JwtProvider.generateToken(auth);
        if (user.getTwoFactorAuth().isEnabled()) {
            AuthResponse response = new AuthResponse();
            response.setMessage("Two factor auth is enabled");
            response.setTwoFactorAuthEnabled(true);
            String otp = OtpUtils.generatedOTP();

            TwoFactorOTP oldTwoFactorOTP = twoFactorOtpService.findbyUser(user.getId());
            if (oldTwoFactorOTP != null) {
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOTP);
            }
            TwoFactorOTP newTwoFactorOTP = twoFactorOtpService.createTwoFactorOTP(user, otp, jwt);
            emailService.sendVerificationOtpEmail(user.getFullName(), otp);
            response.setSession(newTwoFactorOTP.getId());
            return response;
        }
        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setStatus(true);
        response.setMessage("Login success");
        return response;
    }

    @Override
    public User findUserbyJwt(String jwt) {
        String email = JwtProvider.getEmailFromToken(jwt);
        return userRepository.findByEmail(email).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

    }

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

    }

    @Override
    public User enableTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user) {
        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verificationType);

        user.setTwoFactorAuth(twoFactorAuth);
        return userRepository.save(user);
    }

    @Override
    public User updatePassword(User user, String newPassword) {
        user.setPassword(newPassword);
        return userRepository.save(user);

    }


    private Authentication authenticate(String username, String password) {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Override
    public AuthResponse verifyLoginOtp(String otp, String id) {
        TwoFactorOTP twoFactorOTP = twoFactorOtpService.findbyId(id);
        if (twoFactorOtpService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authentication verified");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(twoFactorOTP.getJwt());
            return res;
        }
        throw new IllegalArgumentException("Invalid OTP");
    }

    @Override
    public String sendVerificationOtp(String jwt, VerificationType verificationType) throws MessagingException {
        // Validate and retrieve user from JWT
        User user = findUserbyJwt(jwt);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        // Get or create verification code
        VerificationCode verificationCode =
                verificationCodeService.getVerificationCodeByUser(user.getId());

        if (verificationCode == null) {
            verificationCode = verificationCodeService.sendVerificationCode(user, verificationType);
        }

        // Send OTP via email if needed
        if (verificationType == VerificationType.EMAIL) {
            emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
        }

        return "Verification OTP sent successfully!";
    }

    public User enableTwoFactorAuthentication(String jwt, String otp) {
        User user = findUserbyJwt(jwt);
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }

        VerificationCode verificationCode =
                verificationCodeService.getVerificationCodeByUser(user.getId());

        String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL) ?
                verificationCode.getEmail() : verificationCode.getMobile();

        boolean isVerified = verificationCode.getOtp().equals(otp);

        if (isVerified) {
            User updatedUser = enableTwoFactorAuthentication(verificationCode.getVerificationType(), sendTo, user);
            verificationCodeService.deleteVerificationCodeById(verificationCode);
            return updatedUser;
        }

        throw new AppException(ErrorCode.INVALID_OTP);
    }

    @Override
    public AuthResponse sendForgotPasswordOtp(ForgotPasswordTokenRequest request) throws MessagingException {

        User user = findUserByEmail(request.getSendTo());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_FOUND);
        }
        String otp = OtpUtils.generatedOTP();
        UUID uuid = UUID.randomUUID();
        String id = uuid.toString();

        ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());
        if (token == null) {
            token = forgotPasswordService.createToken(
                    user, id, otp, request.getVerificationType(), request.getSendTo());
        }
        if (request.getVerificationType().equals(VerificationType.EMAIL)) {
            emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());

        }
        AuthResponse authResponse = new AuthResponse();
        authResponse.setSession(token.getId());
        authResponse.setMessage("Password reset otp sent successfully");

        return authResponse;
    }

    @Override
    public ResetPasswordResponse resetPassword(String id, String jwt
            , ResetPasswordRequest request) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);
        boolean isVerified = forgotPasswordToken.getOtp().equals(request.getOtp());
        ResetPasswordResponse response = new ResetPasswordResponse();

        if (isVerified) {
            updatePassword(forgotPasswordToken.getUser(), request.getPassword());
            response.setMessage("password update successfully");
        } else {
            response.setMessage("Wrong otp");
        }
        return response;

    }

}

